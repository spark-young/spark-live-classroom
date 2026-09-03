// vue.config.js
const path = require('path')
module.exports = {
    publicPath: process.env.NODE_ENV === 'production'
        ? './'
        : '/',
    outputDir: "X:\\workspace\\vue\\push\\live",// 输出文件目录
    assetsDir: "./static",//放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录
    indexPath: './index.html',//指定生成的 index.html 的输出路径 (相对于 outputDir)。也可以是一个绝对路径
    lintOnSave: true,
    transpileDependencies: [],
    productionSourceMap: false,
    
    devServer: {
        proxy: {
            '/api': {
                // 此处的写法，目的是为了 将 /api 替换成 https://www.baidu.com/
                // target: 'http://spark-young.top:8088/',//后端服务器前缀
                target: 'http://localhost:8088',
                // 允许跨域
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': ''
                }
            },
            // '/webapi': {
            //     // 此处的写法，目的是为了 将 /api 替换成 https://www.baidu.com/
            //     target: 'http://api.tianapi.com/',//后端服务器前缀
            //     // 允许跨域
            //     changeOrigin: true,
            //     ws: true,
            //     pathRewrite: {
            //         '^/webapi': ''
            //     }
            // }
        }
    }

    // devServer: {  // 开发调试服务器配置项
    //     open: false,  // npm run serve后自动打开页面
    //     host: 'localhost',  // 匹配本机IP地址
    //     port: 8088,  // 开发服务器运行端口号
    //     compress: true,  // 启用静态资源压缩算法
    //     disableHostCheck: true,
    //     proxy: {
    //         '/': {
    //             target: 'http://127.0.0.1:8088/',
    //             changeOrigin: true
    //         }
    //     }
    // }

    
    // 选项...
    // devServer: {
    //     open: true,      // 运行项目时，是否自动开启新窗口。
    //     host: 'localhost',
    //     port: 8080,  // 默认端口号。
    //     https: true,      // 如果开启就会以https开头。
    //     hotOnly: false,      //安装模块更好的兼容，不需要配置。
    //     //     proxy: {
    //     //       '/ee': {
    //     //         target: 'http://localhost:7002/',
    //     //         changeOrigin: true,
    //     //         pathRewrite: {
    //     //           '^/ee': ''
    //     //         }
    //     //       },
    //     //       '/bb': {
    //     //         target: 'http://localhost:8888/',
    //     //         changeOrigin: true,
    //     //         pathRewrite: {
    //     //           '^/bb': ''
    //     //         }
    //     //       }
    //     //     }
    //     //   },
    //     //定制主题
    //     //   css: {
    //     //     loaderOptions: {
    //     //       less: {
    //     //         modifyVars: {
    //     //           'hack': `true; @import "${path.join(__dirname,'./src/assets/style/vant-ui/variables.less')}";`
    //     //         }
    //     //       }
    //     //     }
    //     //   },
    //     configureWebpack: {
    //         resolve: {
    //             alias: {
    //                 'assets': '@/assets',
    //                 'components': '@/components',
    //                 'views': '@/views',
    //             }
    //         }
    //     },
    // }
}

