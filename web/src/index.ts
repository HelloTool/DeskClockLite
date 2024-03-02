import "./styles/index.css"

const timeFormatConfig: Intl.DateTimeFormatOptions = {
  hour: "2-digit",
  minute: "2-digit",
  second: "2-digit",
  hour12: false,
}
const dateFormatConfig: Intl.DateTimeFormatOptions = {
  weekday: "long",
  month: "long",
  day: "numeric",
}
const weekDayMap: string[] = [
  "星期日",
  "星期一",
  "星期二",
  "星期三",
  "星期四",
  "星期五",
  "星期六",
]

const userAgent = navigator.userAgent

let timeFormat: Intl.DateTimeFormat
let dateFormat: Intl.DateTimeFormat
const isIntiSupported = "Intl" in window && userAgent.indexOf("MSIE") > -1
if (isIntiSupported) {
  timeFormat = Intl.DateTimeFormat(undefined, timeFormatConfig)
  dateFormat = Intl.DateTimeFormat(undefined, dateFormatConfig)
}

let lastTime: string
let lastDate: string

/**
 * 通过现代方法获取时间与日期，支持国际化
 */
function getNowTime(dateObj: Date): [time: string, date: string] {
  let time = timeFormat.format(dateObj)
  let date: string
  if ("formatToParts" in dateFormat) {
    date = dateFormat
      .formatToParts(dateObj)
      .map(item => item.value)
      .join(" ")
  } else {
    date = (dateFormat as Intl.DateTimeFormat).format(dateObj)
  }
  return [time, date]
}

/**
 * 传统的获取时间方法，因为部分浏览器不支持Intl
 * @see isIntiSupported
 */
function getNowTimeLegacy(dateObj: Date): [time: string, date: string] {
  let month: number | string = dateObj.getMonth() + 1 //获取月，从 Date 对象返回月份 (0 ~ 11)，故在此处+1
  let day: number | string = dateObj.getDay() //获取日
  let date: number | string = dateObj.getDate() //获取日期
  let hour: number | string = dateObj.getHours() //获取小时
  let minute: number | string = dateObj.getMinutes() //获取分钟
  let second: number | string = dateObj.getSeconds() //获取秒

  //补0
  if (month < 10) month = "0" + month
  if (date < 10) date = "0" + date
  if (hour < 10) hour = "0" + hour
  if (minute < 10) minute = "0" + minute
  if (second < 10) second = "0" + second

  const time = `${hour}:${minute}:${second}`
  const dateText = `${month} 月 ${date} 日 ${weekDayMap[day]}`
  return [time, dateText]
}

function update() {
  const dateObj = new Date()
  const [time, date] = isIntiSupported
    ? getNowTime(dateObj)
    : getNowTimeLegacy(dateObj)

  if (time !== lastTime) {
    document.getElementById("time")!!.innerText = time
    lastTime = time
  }
  if (date !== lastDate) {
    document.getElementById("date")!!.innerText = date
    lastDate = date
  }
}

function onTick() {
  update()
  let timeout = 1000 - new Date().getMilliseconds()
  setTimeout(onTick, timeout)
}
onTick()
