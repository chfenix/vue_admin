<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <div><h2>Configure Data Plan</h2></div>
      <el-row :gutter="40" class="data-plan-group">
        <el-col :xs="16" :sm="16" :lg="16" class="data-plan-col">
          <div class="data-plan-panel">
            <div class="data-plan-header">
              <h3 calss="plan-title">Monthly Pass</h3>
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
          </div>
        </el-col>
        <el-col :xs="8" :sm="8" :lg="8" class="data-plan-col">
          <div class="order-total-panel">
            <h5 style="font-weight:600;font-size: 1.125rem;">Order Total</h5>
            <div class="order-plan">Monthly Pass</div>
            <div style="padding-top: 1rem;color:#646669;">Total</div>
            <h1>$ 50.00  USD</h1>
            <div style="align:center;">
              <el-button type="primary" :loading="loading" class="purchase-button" @click="handleCheckout(1)">Check Out</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { createOrder } from '@/api/order'

export default {
  data() {
    return {
      postData: {
        dataPlanId: '1',
        deviceInfoId: '1'
      },
      loading: false
    }
  },
  methods: {
    handleCheckout(planId) {
      this.loading = true
      createOrder(this.postData).then(() => {
        this.$message.success('Create Order success!')
        // 此处增加调用支付逻辑
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
  padding: 2% 5% 1px;
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
    height: 300px;
    position: relative;
    overflow: hidden;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    padding-right: 14px;
    padding-left: 14px;
  }
  .data-plan-header {
    border-top-left-radius: .2rem;
    border-top-right-radius: .2rem;
    font-size: 1.125rem;
    font-weight: 400;
    text-align: center!important
  }

  .order-total-panel {
    background-color: #35383D;
    border-color: #e7eaf3;
    border-radius: .25rem;
    border: 1px solid #e4e9f3;
    box-shadow: none;
    padding-left: 1rem!important;
    padding-bottom: 1rem!important;
    padding-right: 1rem!important;
    padding-top: 1rem!important;
    color: #f8f9fa!important;
  }

  .order-plan {
    border-bottom: 1px solid 	#646669;
    padding-bottom: 1rem!important;
  }

  .purchase-button {
    font-weight: 600;
    font-size: 1rem;
    background-color: #3b5998;
    width: 100%;
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
