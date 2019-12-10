var path = require('path');

module.exports = {
    entry: './src/main/js/components/app/App.js',
    devtool: 'sourcemaps',
    cache: true,
    mode: 'development',
    resolve: {
        alias: {
            'stompjs': __dirname + '/node_modules' + '/stompjs/lib/stomp.js',
        }
    },
    output: {
        path: path.join(__dirname, 'src/main/resources/static/build'),
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {test: /\.css$/, use: ['style-loader', 'css-loader']},
            {
                test: /\.(gif|png|jpe?g|svg|ico)$/i,
                exclude: /node_modules/,
                use: [
                    {
                        loader: "url-loader",
                        options: {
                            limit: 250000,
                            name: 'images/[hash]-[name].[ext]',
                            publicPath: 'built',
                        },
                    },
                    
                ],
            },
        ]
    },

};