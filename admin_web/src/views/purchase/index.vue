<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <div><h2>Select a Data Plan</h2></div>
      <el-row :gutter="40" class="data-plan-group">
        <el-col :xs="24" :sm="24" :lg="10" class="data-plan-col">
          <div class="data-plan-panel">
            <div class="data-plan-header">
              <h3 calss="plan-title">Monthly Pass</h3>
            </div>
            <div class="plan-price">
              <h1>$50</h1>
            </div>
            <div class="plan-content">
              <div class="plan-desc">
                <ul>
                  <li>每月50G流量</li>
                  <li>最高1Gbps速率</li>
                  <li>覆盖全美所有地区</li>
                </ul>
              </div>
            </div>
            <div class="plan-bottom">
              <el-button type="primary" class="purchase-button" @click="buyNow(1)">BUY NOW</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
    }
  },
  methods: {
    buyNow(planId) {
      this.$router.push('/purchase/checkout')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
  height: 100%;
  padding: 2% 5% 1px;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

.data-plan-group {
  margin-top: 18px;
  .data-plan-col {
    margin-bottom: 32px;
  }

  .purchase-button {
    font-weight: 600;
    font-size: 1rem;
    background-color: #3b5998;
  }
  .plan-bottom {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding-bottom: 1.25rem;
    text-align: center;
  }

  .plan-desc li{
    font-family: Font Awesome\ 5 Free;
    content: "\F058";
    font-weight: 900;
    padding: 6px 0;
    color: #7c8088;
    text-align: left
  }

  .plan-price {
    text-align: center!important;
    font-size: 2.25rem;
    font-weight: 700!important;
  }

  .data-plan-panel {
    height: 500px;
    // cursor: pointer;
    position: relative;
    overflow: hidden;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    padding-right: 14px;
    padding-left: 14px;

    &:hover {
      .data-plan-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }
  }
  .data-plan-header {
    border-top-left-radius: .2rem;
    border-top-right-radius: .2rem;
    font-size: 1.125rem;
    font-weight: 400;
    text-align: center!important
  }
}

@media (max-width:550px) {
  .data-plan-panel-description {
    display: none;
  }

  .data-plan-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
