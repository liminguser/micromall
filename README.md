# MicroMall

MicroMall 是一个基于 Spring Boot 和 Vue.js 的微型商城系统。

## 技术栈

### 后端
- Spring Boot
- MyBatis-Plus
- MySQL
- Maven

### 前端
- Vue 3
- Element Plus
- Vite
- Pinia

## 功能特性

- 用户认证
  - 登录/注册
  - JWT 认证
- 商品管理
  - 商品列表
  - 商品详情
  - 商品分类
- 购物车
  - 添加商品
  - 修改数量
  - 删除商品
  - 清空购物车
- 订单管理 (开发中)

## 快速开始

### 后端

1. 克隆项目
```bash
git clone https://github.com/liminguser/micromall.git
```

2. 配置数据库
- 创建 MySQL 数据库
- 修改 `application.yml` 中的数据库配置

3. 运行后端
```bash
cd backend/microMall
mvn spring-boot:run
```

### 前端

1. 安装依赖
```bash
cd frontend/microMall
npm install
```

2. 运行开发服务器
```bash
npm run dev
```

## 项目结构

```
micromall/
├── backend/
│   └── microMall/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   └── resources/
│       │   └── test/
│       └── pom.xml
└── frontend/
    └── microMall/
        ├── src/
        │   ├── api/
        │   ├── assets/
        │   ├── components/
        │   ├── layout/
        │   ├── router/
        │   ├── stores/
        │   ├── utils/
        │   └── views/
        ├── index.html
        └── package.json
```

## 贡献

欢迎提交 Issue 和 Pull Request。

## 许可证

[MIT License](LICENSE)