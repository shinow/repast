# 基于springboot和dubbo开发的平台服务
dubbo+spring boot+springJPA

服务结构
--repast-parent  父项目
----repast-common-api  公用的api接口包
----repast-common-service  公用的服务提供者
----repast-elasticsearch-api  搜索引擎api接口包
----repast-elasticsearch-service  搜索引擎服务提供者
----repast-merchant-api  商户的api接口包(商户信息和商户后台相关信息)
----repast-merchant-service  商户服务提供者
----repast-merchant-web  商户后台web处理系统
----repast-order-api  订单的api接口包
----repast-order-service  订单的服务提供者
----repast-pay  支付接口父项目
------repast-pay-api  支付api接口包
------repast-pay-service  支付服务提供者
------repast-pay-notify  支付通知处理服务
----repast-platform-api  平台的api接口包
----repast-platform-service  平台的服务提供者
----repast-platform-web  平台后台web处理系统
----repast-weixin-web  前端微信web处理系统
----repast-scheduled-service  定时任务处理服务
----repast-app-rest  前端APP的rest接口服务

maven的配置文件在setting.xml，将其复制到本地的maven的conf中覆盖，
在点击eclispe的Window的Perferences的Maven配置User Settings关联到本地maven的setting.xml
