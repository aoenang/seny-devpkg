# seny-devpkg

**seny-devpkg：**
- utils:常见工具类。
- net：对volley的二次封装。包含json解析，json缓存，图片缓存等处理。

    1. 实现了json请求字符串的缓存和读取
    2. 使用LruCache和DiskLruCache实现ImageCache接口
    3. HttpLoader负责GsonRequest的创建和管理(重复请求处理，错误请求通用处理)
    4. 管理的GsonRequest请求增加Request Code标识，便于UI层区分请求处理。
    5. 同样支持其他Request的发起(比如StringRequest,JsonRequest等)

- view: 通用的自定义View
- base：通用的Base基类。

**sample:**
1. 演示如果将seny-devpkg作为一个app的基本框架进行网络开发。