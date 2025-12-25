<template>
  <div id="app">
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="logo-section">
            <h1>🤖 AI Code Review</h1>
          </div>
          <el-menu
            :default-active="$route.path"
            mode="horizontal"
            router
            background-color="transparent"
            text-color="#fff"
            active-text-color="#409EFF"
            class="nav-menu"
          >
            <el-menu-item index="/projects">
              <Icon name="repository" />
              <span>项目管理</span>
            </el-menu-item>
            <el-menu-item index="/pull-requests">
              <Icon name="operation" />
              <span>PR审核</span>
            </el-menu-item>
            <el-menu-item index="/statistics">
              <Icon name="dataAnalysis" />
              <span>统计分析</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-header>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import Icon from '@/components/Icon.vue'
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
}

.el-container {
  height: 100%;
}

.el-header {
  background-color: #304156;
  color: #fff;
  padding: 0;
  height: 60px !important;
  line-height: 60px;
}

.header-content {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  width: 100%;
}

.logo-section {
  margin-right: 50px;
}

.logo-section h1 {
  margin: 0;
  font-size: 24px;
  white-space: nowrap;
  font-weight: 500;
}

.nav-menu {
  flex: 1;
  display: flex;
  justify-content: flex-start;
}

.el-menu {
  border-right: none;
  background-color: transparent !important;
}

.el-menu.el-menu--horizontal {
  border-bottom: none;
}

.el-menu-item {
  height: 60px;
  line-height: 60px;
  font-size: 15px;
  border-bottom: 2px solid transparent !important;
  padding: 0 20px;
  display: flex;
  align-items: center;
}

.el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

.el-menu-item.is-active {
  background-color: rgba(255, 255, 255, 0.1) !important;
  border-bottom-color: #409EFF !important;
}

.el-menu-item .el-icon {
  margin-right: 5px;
  vertical-align: middle;
  font-size: 18px;
}

.el-menu-item span {
  font-size: 15px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
  min-height: calc(100vh - 60px);
}

body {
  margin: 0;
  padding: 0;
}

/* 响应式设计 */
@media screen and (max-width: 1200px) {
  .logo-section {
    margin-right: 30px;
  }

  .logo-section h1 {
    font-size: 22px;
  }

  .el-menu-item {
    padding: 0 15px;
  }
}

@media screen and (max-width: 768px) {
  .logo-section {
    margin-right: 20px;
  }

  .logo-section h1 {
    font-size: 20px;
  }

  .el-menu-item {
    padding: 0 12px;
    font-size: 14px;
  }

  .el-menu-item span {
    font-size: 14px;
  }
}

@media screen and (max-width: 600px) {
  .header-content {
    flex-direction: column;
    height: auto;
    padding: 15px;
  }

  .el-header {
    height: auto !important;
  }

  .logo-section {
    margin-right: 0;
    margin-bottom: 15px;
  }

  .logo-section h1 {
    font-size: 22px;
    text-align: center;
  }

  .nav-menu {
    width: 100%;
  }

  .el-menu {
    width: 100%;
    justify-content: center;
  }

  .el-menu-item {
    flex: 1;
    justify-content: center;
    padding: 0 10px;
  }

  .el-main {
    min-height: calc(100vh - 120px);
  }
}
</style>