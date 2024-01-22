// Generated using webpack-cli https://github.com/webpack/webpack-cli

import path from 'path';
import HtmlWebpackPlugin from 'html-webpack-plugin';
import MiniCssExtractPlugin from 'mini-css-extract-plugin';
import WorkboxWebpackPlugin from 'workbox-webpack-plugin';


const isProduction = process.env.NODE_ENV == 'production';


const stylesHandler = MiniCssExtractPlugin.loader;

/**
 * @type {import('webpack').Configuration}
 */
const config = {
    entry: './src/index.ts',
    output: {
        path: path.resolve(import.meta.dirname, 'dist'),
    },
    devServer: {
        host: 'localhost',
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './src/index.html',
            inject: 'body',
            scriptLoading: 'blocking'
        }),

        new MiniCssExtractPlugin(),

        // Add your plugins here
        // Learn more about plugins from https://webpack.js.org/configuration/plugins/
    ],
    module: {
        rules: [{
            test: /\.css$/i,
            use: [stylesHandler, 'css-loader', 'postcss-loader'],
        },
        {
            test: /\.(ts|tsx)$/i,
            loader: 'ts-loader',
            exclude: ['/node_modules/'],
        },

        {
            test: /\.(eot|svg|ttf|woff|woff2|png|jpg|gif)$/i,
            type: 'asset',
        },

            // Add your rules for custom modules here
            // Learn more about loaders from https://webpack.js.org/loaders/
        ],
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.jsx', '.js', '...'],
    },
};

export default () => {
    if (isProduction) {
        config.mode = 'production';
        config.plugins.push(new WorkboxWebpackPlugin.GenerateSW());
    } else {
        config.mode = 'development';
    }
    return config;
};
