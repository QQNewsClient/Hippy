/* eslint-disable no-param-reassign */
import { getEventRedirector } from '@hippy/vue-native-components/src/utils';

export default {
  install(Vue) {
    Vue.registerElement('hi-waterfall', {
      component: {
        name: 'WaterfallView',
        processEventData(event, nativeEventName, nativeEventParams) {
          // know events: onEndReached、onHeaderReleased、onHeaderPulling、initialListReady、onExposureReport
          if (nativeEventParams.contentOffset) {
            event.conentOffset = nativeEventParams.contentOffset;
          }
          if (nativeEventParams.exposureInfo) {
            event.exposureInfo = nativeEventParams.exposureInfo;
          }
          return event;
        },
      },
    });
    Vue.registerElement('hi-waterfall-item', {
      component: {
        name: 'WaterfallItem',
      },
    });

    // eslint-disable-next-line vue/one-component-per-file
    Vue.component('waterfall', {
      inheritAttrs: false,
      props: {
        numberOfColumns: {
          type: Number,
          default: 2,
        },
        numberOfItems: {
          type: Number,
          required: true,
        },
        contentInset: {
          type: Object,
          default: () => ({ top: 0, left: 0, bottom: 0, right: 0 }),
        },
        scrollEventThrottle: {
          type: Number,
          default: 0,
        },
        columnSpacing: {
          type: Number,
          default: 0,
        },
        interItemSpacing: {
          type: Number,
          default: 0,
        },
        seperatorStyle: {
          type: String,
          default: 'none',
        },
        preloadItemNumber: {
          type: Number,
          default: 0,
        },
        initialListSize: {
          type: Number,
          default: 0,
        },
        containBannerView: {
          type: Boolean,
          default: false,
        },
        containPullHeader: {
          type: Boolean,
          default: false,
        },
        containPullFooter: {
          type: Boolean,
          default: false,
        },
      },
      methods: {
        onEndReached() {
          this.$emit('onEndReached');
        },
        onHeaderReleased() {
          this.$emit('onHeaderReleased');
        },
        onHeaderPulling() {
          this.$emit('onHeaderPulling');
        },
        onInitialListReady() {
          this.$emit('onInitialListReady');
        },
        onExposureReport() {
          this.$emit('onExposureReport');
        },

        // native methods
        call(action, params) {
          Vue.Native.callUIFunction(this.$refs.waterfall, action, params);
        },
        refreshCompleted({ status, text, duration, imageUrl }) {
          this.call('refreshCompleted', [status, text, duration, imageUrl]);
        },
        startRefresh() {
          this.call('startRefresh');
        },
        /** @param {Number} type 1.同startRefresh */
        startRefreshWithType(type) {
          this.call('startRefreshWithType', [type]);
        },
        callExposureReport() {
          this.call('callExposureReport', []);
        },
        scrollToIndex({ index, animation }) {
          // iOS用xIndex，安卓用yIndex
          this.call('scrollToIndex', [index, index, animation]);
        },
        scrollToContentOffset({ x, y, animation }) {
          this.call('scrollToContentOffset', [x, y, animation]);
        },
        startLoadMore() {
          this.call('scrollToContentOffset');
        },
      },
      render(h) {
        const on = getEventRedirector.call(this, [
          ['onEndReached', 'endReached'],
          ['onHeaderReleased', 'headerReleased'],
          ['onHeaderPulling', 'headerPulling'],
          ['onExposureReport', 'exposureReport'],
          ['onInitialListReady', 'initialListReady'],
        ]);
        return h(
          'hi-waterfall',
          {
            on,
            ref: 'waterfall',
            attrs: {
              numberOfColumns: this.numberOfColumns,
              numberOfRows: this.numberOfItems,
              contentInset: this.contentInset,
              scrollEventThrottle: this.scrollEventThrottle,
              columnSpacing: this.columnSpacing,
              interItemSpacing: this.interItemSpacing,
              seperatorStyle: this.seperatorStyle,
              preloadItemNumber: this.preloadItemNumber,
              initialListSize: this.initialListSize,
              containBannerView: this.containBannerView,
              containPullHeader: this.containPullHeader,
              containPullFooter: this.containPullFooter,
            },
          },
          this.$slots.default,
        );
      },
    });

    // eslint-disable-next-line vue/one-component-per-file
    Vue.component('waterfall-item', {
      inheritAttrs: false,
      props: {
        type: {
          type: [String, Number],
          default: '',
        },
      },
      render(h) {
        return h(
          'hi-waterfall-item',
          {
            on: { ...this.$listeners },
            attrs: {
              type: this.type,
            },
          },
          this.$slots.default,
        );
      },
    });

    Vue.registerElement('hi-hippy-header-refresh', {
      component: {
        name: 'PullHeaderView',
      },
    });
  },

};