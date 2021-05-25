<template>
  <div id="demo-waterfall">
    <ul-refresh-wrapper ref="header" style="flex:1;" @refresh="onRefresh">
    <ul-refresh class="refresh-header">
      <p class="refresh-text">{{ refreshText }}</p>
    </ul-refresh>

    <waterfall
      ref="gridView"
      :contain-pull-header="false"
      :contain-pull-footer="false"
      :contain-banner-view="false"
      :inter-item-spacing="interItemSpacing"
      :column-spacing="columnSpacing"
      :number-of-columns="numberOfColumns"
      :number-of-items="dataSource.length"
      :preload-item-number="0"
      :scroll-event-throttle="200"
      :seperator-style="'none'"
      :style="{flex: 1}"
      @endReached="onEndReached"
    >
      <waterfall-item
        v-for="(ui, index) in dataSource"
        :key="index"
        :type="'item-' + ui.style"
        :style="{width: itemWidth}"
      >
        <style-one v-if="ui.style == 1" :itemBean="ui.itemBean" />
        <style-two v-if="ui.style == 2" :itemBean="ui.itemBean" />
        <style-five v-if="ui.style == 5" :itemBean="ui.itemBean" />
      </waterfall-item>
    </waterfall>

    </ul-refresh-wrapper>

  </div>
</template>

<script>
import Vue from 'vue';
import mockData from '../list-items/mock';
import '../list-items';

const STYLE_LOADING = 100;
const MAX_FETCH_TIMES = 50;

export default {
  data() {
    return {
      dataSource: [],
      isRefreshing: false,
      headerOffset: 0,
      Vue,
      STYLE_LOADING,
    };
  },
  mounted() {
    // *** isLoading 是加载锁，业务请照抄 ***
    // 因为 onEndReach 位于屏幕底部时会多次触发，
    // 所以需要加一个锁，当未加载完成时不进行二次加载
    this.isLoading = false;
    this.dataSource = [...mockData, ...mockData, ...mockData, ...mockData];
  },
  computed: {
    refreshText() {
      return this.isRefreshing ? '正在刷新' : '下拉刷新';
    },

    itemWidth() {
      const width = 375 - this.contentInset.left - this.contentInset.right;
      return (width - ((this.numberOfColumns - 1) * this.columnSpacing)) / this.numberOfColumns;
    },
    columnSpacing() {
      return 6;
    },
    interItemSpacing() {
      return 6;
    },
    numberOfColumns() {
      return 2;
    },
    contentInset() {
      return { top: 0, left: 0, bottom: 0, right: 0 };
    },
  },
  methods: {
    mockFetchData() {
      return new Promise((resolve) => {
        setTimeout(() => {
          this.fetchTimes += 1;
          if (this.fetchTimes >= MAX_FETCH_TIMES) {
            return resolve(null);
          }
          return resolve([...mockData, ...mockData, ...mockData, ...mockData]);
        }, 300);
      });
    },
    async onRefresh() {
      // 重新获取数据
      this.isRefreshing = true;
      const dataSource = await this.mockFetchData();
      await (new Promise(resolve => setTimeout(() => resolve(), 3000)));
      this.isRefreshing = false;
      this.dataSource = dataSource.reverse();
      this.$refs.header.refreshCompleted();
    },
    async onEndReached() {
      const { dataSource } = this;
      // 检查锁，如果在加载中，则直接返回，防止二次加载数据
      if (this.isLoading) {
        return;
      }

      this.isLoading = true;
      this.loadingState = '正在加载...';

      const newData = await this.mockFetchData();
      if (!newData) {
        this.loadingState = '没有更多数据';
        this.isLoading = false;
        return;
      }

      this.loadingState = '';
      this.dataSource = [...dataSource, ...newData];
      this.isLoading = false;
    },
  },
};
</script>

<style scoped>
  #demo-waterfall {
    flex: 1;
  }

  #demo-waterfall .refresh-header {
    background-color: green;
  }

  #demo-waterfall .refresh-text {
    height: 60px;
    line-height: 60px;
    text-align: center;
    color: white;
  }

  #demo-waterfall .list-view-item {
    background-color:#eeeeee;
  }

  #demo-waterfall .article-title {
    font-size: 12px;
    line-height: 16px;
    color: #242424;
  }

  #demo-waterfall .normal-text {
    font-size: 10px;
    color: #aaa;
    align-self: center;
  }

  #demo-waterfall .image {
    flex: 1;
    height: 120px;
    resize: both;
  }

  #demo-waterfall .style-one-image-container {
    flex-direction: row;
    justify-content: center;
    margin-top: 8px;
    flex: 1;
  }

  #demo-waterfall .style-one-image {
    height: 60px;
  }

  #demo-waterfall .style-two {
    flex-direction: row;
    justify-content: space-between;
  }

  #demo-waterfall .style-two-left-container {
    flex: 1;
    flex-direction: column;
    justify-content: center;
    margin-right: 8px;
  }

  #demo-waterfall .style-two-image-container {
    flex: 1;
  }

  #demo-waterfall .style-two-image {
    height: 80px;
  }

  #demo-waterfall .refresh {
    background-color: green;
  }

</style>
