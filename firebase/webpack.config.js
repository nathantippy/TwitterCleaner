var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: './src/index.js',
    module: {
        rules: [
            { 
                test: /\.jsx?$/, 
                loader: 'babel-loader', 
                exclude: /node_modules/, 
                query: { 
                    presets: ['es2015', 'react', 'stage-0']
                }
            },
            { 
                test: /\.css$/, 
                use:[
                    {
                        loader: 'css-loader',
                        options: {
                            modules: true,
                            localIdentName: '[name]__[local]__[hash:base64:5]'
                        }
                    }
                ]
            },
            { 
                test: /\.scss$/, 
                use:[
                    {loader: 'style-loader'},
                    {
                        loader: 'css-loader',
                        options: {
                            modules: true,
                            sourceMap: true,
                            importLoaders: 2,
                            localIdentName: '[name]__[local]__[hash:base64:5]'
                        }
                    },
                    {loader: 'sass-loader'}
                ]
            }
        ]
    },
    output: {
        filename: 'bundle.js',
        path: __dirname + '/public'
    },
    resolve: {
        extensions: ['.js', '.jsx']
    }
};