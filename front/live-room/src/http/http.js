/**
 * 与后端交互的封装，http.js装载所有访问方法（get、post）
 */

import axios from "axios";
axios.defaults.timeout = 5000; // 请求超时
// axios.defaults.baseURL = "http://127.0.0.1:8088/"; // 增加请求默认路径前缀
axios.defaults.baseURL = "/api"

export default {
  /**
   * get 请求
   * @param apiName 接口名
   * @returns {AxiosPromise<any>}
   */
  get(apiName,body) {
    let url = apiName.url
    let options = {};
    let headers = {
    }
    headers['Content-Type'] = 'application/json'
    headers['Access-Control-Allow-Origin'] = '*'
    // headers['Access-Control-Allow-Headers'] = 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild'
    options.headers = headers

    if (body) {
      options.data = body;
    }
    console.log("axios请求"+url);
    return axios.get(url, options);
  },

  /**
   * post 请求
   *
   * @param url 接口路由
   * @param params 接口参数
   * @returns {AxiosPromise<any>}
   */
  post(apiName, data) {
    let url = apiName.url
    let options = {};
    let headers = {}
    headers['Content-Type'] = 'application/json'
    options.headers = headers
    console.log("axios请求"+url);
    return axios.post(url, data, options);
  }
};