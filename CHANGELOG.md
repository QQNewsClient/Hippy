# Change Log

All notable changes to this project will be documented in this file.
See [Conventional Commits](https://conventionalcommits.org) for commit guidelines.

## [2.5.5](https://github.com/Tencent/Hippy/compare/2.5.4...2.5.5) (2021-03-26)


### Bug Fixes

* **android:** fetch resource with https:// ([c3dee7c](https://github.com/Tencent/Hippy/commit/c3dee7ca8bf7302f5aecc263b6e77624249073a6))
* **android:** should catch resource NotFoundException ([6284a49](https://github.com/Tencent/Hippy/commit/6284a49d05582825cd158fb73f56f335ac9a5af8))
* **android:** support engine status enum to int ([9708892](https://github.com/Tencent/Hippy/commit/970889220fa4eeb011c9e8c9cd443a125d025ad9))
* **android:** use ByteBuffer for onResourceReady ([9863317](https://github.com/Tencent/Hippy/commit/9863317dd89a6cb3a4d3242564920a4da8af7f02))
* **core:** fix conflict resolve error ([903650a](https://github.com/Tencent/Hippy/commit/903650a824a5b2fb14db3cfc47347bff50227469))
* **core:** fix dynamic load crash ([f9138b2](https://github.com/Tencent/Hippy/commit/f9138b2fcb203b0af5820694ab1ca8a2b231e070))
* **core:** fix iOS CreateJsError function error ([df8355c](https://github.com/Tencent/Hippy/commit/df8355cbc05914aba9b0c87146192809c92a8d0a))
* **core:** fix jni crash ([#651](https://github.com/Tencent/Hippy/issues/651)) ([6ae32cf](https://github.com/Tencent/Hippy/commit/6ae32cf835fe8ebc218a897d34b8836e0e1151ec))
* **core:** format ([618a40a](https://github.com/Tencent/Hippy/commit/618a40a1f4f667d8a1fd1f49358a38a876e5b270))
* **core:** revert v8 refactor ([82b0b27](https://github.com/Tencent/Hippy/commit/82b0b272971f2832ea5d74b21c57b646a1fc1e06))
* **core:** update so ([acd5bd1](https://github.com/Tencent/Hippy/commit/acd5bd1e124f63c1f4d8a4dd5ff9d295489e9722))
* **docs:** fix dynamic import docs ([d4a553d](https://github.com/Tencent/Hippy/commit/d4a553d34666bd548723e3a26ab2cf233981bdfa))
* **ios:** fix bug for touch handler ([15d0bb3](https://github.com/Tencent/Hippy/commit/15d0bb3a39cb44497069f8a7f1a58121fd65b6c5))
* **ios:** fix dynamic load for local file ([130b9d5](https://github.com/Tencent/Hippy/commit/130b9d5c622797b02915f688f11684a4fc260ef4))
* **ios:** fix index exceed range of array ([34591dd](https://github.com/Tencent/Hippy/commit/34591ddfb123490adac08f7c2d86f16be7219295))
* **ios:** improve stability ([3dc0743](https://github.com/Tencent/Hippy/commit/3dc074388c03ef0068bf98c21b600f3200839e4f))


### Features

* **android:** adjust for wormhole ([09fb96b](https://github.com/Tencent/Hippy/commit/09fb96b4b532ba5ae3daa11ec1f9c220d4a5ff87))
* **core:** daymiacLoad support http req ([#640](https://github.com/Tencent/Hippy/issues/640)) ([e7cd1f3](https://github.com/Tencent/Hippy/commit/e7cd1f3896c2fc287730ac24778732cd2704b976))
* **core:** refactor dynamic jni ([2f627df](https://github.com/Tencent/Hippy/commit/2f627df6d1f92e0ca11c93d4924f401f970d3c32))
* **core:** support multi scheme dynamic load ([932b51c](https://github.com/Tencent/Hippy/commit/932b51c952aca14150d153366a6695c078ef3a97))
* **ios:** adapter dynamic load ([f53bdd1](https://github.com/Tencent/Hippy/commit/f53bdd1fc4b73635fd211f677bba8a1fecf0e2c8))





## [2.5.4](https://github.com/Tencent/Hippy/compare/2.5.3...2.5.4) (2021-03-17)


### Bug Fixes

* **android:** add d8 setting for qq ([e001cdf](https://github.com/Tencent/Hippy/commit/e001cdfd1f5b9823da5bd6afa16958ba572a63c1))
* **android:** as inspect code ([357aac2](https://github.com/Tencent/Hippy/commit/357aac29d74bf4d66c533ea2ef8e6a6efb7de849))
* **android:** demo add onResume and onStop ([8435397](https://github.com/Tencent/Hippy/commit/8435397c7f85f0364e49b202da89f1642e60930e))
* **android:** run fetch image on main thread ([bd3479e](https://github.com/Tencent/Hippy/commit/bd3479e29768c4ecdba6d0b54cf89cd3da222261))
* **core:** add explicit ([22d3601](https://github.com/Tencent/Hippy/commit/22d360177162475bee7cc104aea4dc2ee1f11af3))
* **core:** fix codecc ([f8212e4](https://github.com/Tencent/Hippy/commit/f8212e49ae7bac63590de6622447dfc50c9e9df4))
* **core:** fix codecc warning ([#608](https://github.com/Tencent/Hippy/issues/608)) ([1077a79](https://github.com/Tencent/Hippy/commit/1077a7962a717f5151a8c3cc5da310d241560ce4))
* **core:** fix scope nullptr bug ([3df0e8c](https://github.com/Tencent/Hippy/commit/3df0e8c229c0a2ef9abb442701e45993ba2f8671))
* **hippy-vue:** add child animation destroy logic ([e0404fa](https://github.com/Tencent/Hippy/commit/e0404fa8cb31612309c39c16d8fe8a967fac5300))


### Features

* **core:** adapter 89 chrome dev tools ([#626](https://github.com/Tencent/Hippy/issues/626)) ([bab3d67](https://github.com/Tencent/Hippy/commit/bab3d67b632f867c4124396cac0c353d91e027db))
* **ios:** add onInterceptPullUpEvent propery ([d46432f](https://github.com/Tencent/Hippy/commit/d46432f9e4a5c9ad4cc7ceb0193c55a7c9967e6d))
* **layout:** format files ([5d18951](https://github.com/Tencent/Hippy/commit/5d18951a547efd1ea6d116f0704a863b5753fa0b))





## [2.5.3](https://github.com/Tencent/Hippy/compare/2.5.2...2.5.3) (2021-03-10)


### Bug Fixes

* **android:** add maven publish ([f7c4552](https://github.com/Tencent/Hippy/commit/f7c4552c4725132947fdcc45be3e35227ed84d69))
* **android:** call super.onDestroy() ([0f7e2d9](https://github.com/Tencent/Hippy/commit/0f7e2d945ce3b4688e6742ebb0ce4ee26179c31b))
* **android:** get thirdParty packageName first ([3fb32f3](https://github.com/Tencent/Hippy/commit/3fb32f3b49256b7b78ad2d5abcd6115150d94ba8))
* **android:** mListScrollListeners ConcurrentModificationException ([d8b2ab2](https://github.com/Tencent/Hippy/commit/d8b2ab2b846f30bf11f6423eb27eda286abf1df1))
* **android:** should catch UnsatisfiedLinkError for runScriptFromUri ([28d0a02](https://github.com/Tencent/Hippy/commit/28d0a02df34c5eb497a0a4f7c38aece511848435))
* **core:** fix DLOG macro ([9eb0bb8](https://github.com/Tencent/Hippy/commit/9eb0bb8ce066e34404a8629d5c411f63c920ce44))
* **ios:** fix HippyRootView did load finish callback ([e646db5](https://github.com/Tencent/Hippy/commit/e646db5479031ac93f85ab04f78a23a09d67e406))
* **ios:** try catch malloc exception when make image blurred ([#603](https://github.com/Tencent/Hippy/issues/603)) ([cc2521b](https://github.com/Tencent/Hippy/commit/cc2521bd6cff2a676c363f03e55ef5a9761063d0))


### Features

* **core:** avoid crash when the jni method does not exist ([05ae0cd](https://github.com/Tencent/Hippy/commit/05ae0cd4d02a8aae24f10f0c51da4c052966ce9f))





## [2.5.2](https://github.com/Tencent/Hippy/compare/2.5.1...2.5.2) (2021-03-08)


### Bug Fixes

* **android:** add onPreload for preloadItemNumber ([9d94837](https://github.com/Tencent/Hippy/commit/9d94837ed51c1ca46fa5db8fe548be014f99f30d))
* **android:** code style of HippyListAdapter ([66b4a39](https://github.com/Tencent/Hippy/commit/66b4a39e77b4f08fa25b120cfa47f030d5242585))
* **android:** ignore ClassNotFoundException for add video controller ([664dd7a](https://github.com/Tencent/Hippy/commit/664dd7a1f6bb60e322e53bd3ed29e22e8020e7fd))
* **android:** keep setCustomProp handle ([30d24b5](https://github.com/Tencent/Hippy/commit/30d24b5aa40eb871c16a2e2d31a19c914e3ec0da))
* **android:** rdm exception bug reports ([afcfc0c](https://github.com/Tencent/Hippy/commit/afcfc0c1bb8b022fa32b604aa55ffb95474a6d0a))
* **android:** some code style problem ([2246f53](https://github.com/Tencent/Hippy/commit/2246f5339c45297961f85d648de59ea91291bf23))
* **android:** use LogUtils replace Log ([56135da](https://github.com/Tencent/Hippy/commit/56135da02d18cafbdb2c72c41368c4e46ec1bdca))
* **ios:** fix support for translateZ ([6de05fa](https://github.com/Tencent/Hippy/commit/6de05fa5fd7eb4e1e3960f8e9f6ea4487d69ce23))
* **ios:** implement backgroundSize setter for HippyImageView ([b7ff2b8](https://github.com/Tencent/Hippy/commit/b7ff2b89d758d3d3501fd724a8b486fed18043db))


### Features

* **hippy-vue:** add animation event & method ([4018df8](https://github.com/Tencent/Hippy/commit/4018df8d8873e5830182b7d567e837c9bc5a0ef1))





## [2.5.1](https://github.com/Tencent/Hippy/compare/2.5.0...2.5.1) (2021-03-02)


### Bug Fixes

* **android:** add gradle features setting, remove PRODUCT_FLAVORS ([6e0c501](https://github.com/Tencent/Hippy/commit/6e0c501f9b06125d688f4b0f417f6ea0dbb8c906))
* **android:** fix TextNode width when do StaticLayout ([9942a2a](https://github.com/Tencent/Hippy/commit/9942a2a107fcc08f8dbc83c3534feb97d7e57dca))
* **android:** fix ViewPager setScrollEnabled not works ([cf5514b](https://github.com/Tencent/Hippy/commit/cf5514b352874aee065110647d7ab22b103fe5ed))
* **android:** resolve touch conflict for nested ViewPager ([aa4ac15](https://github.com/Tencent/Hippy/commit/aa4ac155f95e14fff814743ba88fdf5bb73967f7))
* **hippy-vue:** fixed repeatCount error in animationSet ([c16f306](https://github.com/Tencent/Hippy/commit/c16f3069d1361d69d872acf9476f345c0f886364))
* **ios:** invoke jscexecutor.invalide on js queue ([35262c4](https://github.com/Tencent/Hippy/commit/35262c4ff7e32df6e385e6fc824f1c9a456f5509))


### Features

* **android:** listview add props : overScrollEnabled ([3ed9418](https://github.com/Tencent/Hippy/commit/3ed9418b61e447365f0becdda6274c536a931c9d))





# [2.5.0](https://github.com/Tencent/Hippy/compare/2.4.0...2.5.0) (2021-02-25)


### Bug Fixes

* **ios:** fix viewpager bug ([54b0e06](https://github.com/Tencent/Hippy/commit/54b0e060b1dc078d83f830ce3a9b42df210dec56))


### Reverts

* Revert "feat(hippy-core): export so symbol" ([94842f6](https://github.com/Tencent/Hippy/commit/94842f69c5001cf69226848978e93703a3828c1c))





# [2.4.0](https://github.com/Tencent/Hippy/compare/2.3.4...2.4.0) (2021-02-25)


### Bug Fixes

* **android:** add usesCleartextTraffic=true ([6c21253](https://github.com/Tencent/Hippy/commit/6c2125336fadc85df86743b62874bf7393bc3d3c))
* **android:** merge some bug fix from QB branch ([5ad10ae](https://github.com/Tencent/Hippy/commit/5ad10ae476ad8178996cf5ada0e11115c196edc0))
* **android:** merge some bug fix from QQ branch ([a9b9460](https://github.com/Tencent/Hippy/commit/a9b94603627612f4b84a0100345a7a898a3bb385))
* **android:** onCreateSuspendViewHolderWithPos should return null ([7efa9c9](https://github.com/Tencent/Hippy/commit/7efa9c9a980dc54ba879e1a7c9a4ed3536a37f27))
* **android:** support only include armeabi ([3e60c4d](https://github.com/Tencent/Hippy/commit/3e60c4d9e02ef20e2ad7731a23eec73eab75f208))
* **android:** update v8 77 so ([a05fdaf](https://github.com/Tencent/Hippy/commit/a05fdaf8c1cc069c560a44fbe96e7807e1432023))
* **android:** webview set allow content access to false ([b12e5cf](https://github.com/Tencent/Hippy/commit/b12e5cfd25008ecdb29c780a250ab5a58087175c))
* **core:** avoid iOS map crash ([7657ab5](https://github.com/Tencent/Hippy/commit/7657ab53bc2c7ee998bed8cbd52f923996843e04))
* **core:** fix debug crash ([748c6de](https://github.com/Tencent/Hippy/commit/748c6dede274f423c21c955f003f7cc7a751d046))
* **core:** fix js exception report bug ([1b27277](https://github.com/Tencent/Hippy/commit/1b27277be0e4060d6480aea835c4d8c832bc6f70))
* **feat:** change build-core script ([38b99dd](https://github.com/Tencent/Hippy/commit/38b99dd9510da2e4c4ba4fd7525f78398cc9a0e5))
* **hippy-core:** remove getOwnPropertyDescriptors ([58f5ff7](https://github.com/Tencent/Hippy/commit/58f5ff7729b57c246ccce7f72d103e5af6498165))
* **hippy-react-web:** correct margin and padding style values ([#543](https://github.com/Tencent/Hippy/issues/543)) ([0754a68](https://github.com/Tencent/Hippy/commit/0754a682ffe894f36c48b1a739c65ab8b76e9a24))
* **hippy-react,hippy-vue:** add examples webpack sourcetype ([839da97](https://github.com/Tencent/Hippy/commit/839da975819024647a9797a836792dabeeb19acf))
* **hippy-vue:** fix vue webpack dll problem ([bbdc95c](https://github.com/Tencent/Hippy/commit/bbdc95cd602d8d545a2958cd1b877905300b1c4d))
* **hippy-vue,hippy-react:** added example babel plugins ([cd54ce9](https://github.com/Tencent/Hippy/commit/cd54ce91082f81257095d7979ea6ca717bd323f9))
* **hippy-vue,hippy-react:** change webpack vendor script ([4ac88cb](https://github.com/Tencent/Hippy/commit/4ac88cbf03ec4ea34538ce27b850f8d4ab1c24be))
* **ios:** add [@autoreleasepool](https://github.com/autoreleasepool) in hippy custom js thread callback block ([b0518a2](https://github.com/Tencent/Hippy/commit/b0518a25dd0f46a69ac78cb6261cbe8ca661dc63))
* **ios:** fix apng image delay time error ([f8eb557](https://github.com/Tencent/Hippy/commit/f8eb557ab92167bd9351d149078373e3ceeebdd7))
* **ios:** fix backgroundSize property of View component ([e16cedf](https://github.com/Tencent/Hippy/commit/e16cedf9301194ec22cfe312dfef11a9cf5cf6a8))
* **ios:** fix device orientation change event bug ([d80c6a3](https://github.com/Tencent/Hippy/commit/d80c6a36f0a6aa17345740571c72b2fa140dfc84))
* **ios:** fix image down sample error ([b967ceb](https://github.com/Tencent/Hippy/commit/b967cebad80c9eb55f3fc3dbade3bf9d7b697324))
* **ios:** fix image insets error ([9651d84](https://github.com/Tencent/Hippy/commit/9651d8412d9ed234a984d8321ee39d8cec22d6b0))
* **ios:** fix potential crash because of a nil context pointer ([e8c1820](https://github.com/Tencent/Hippy/commit/e8c18208376715a636d2288df123d86b44e7d8af))
* **ios:** fix protential crash when AppDelegate.window called ([5048b0b](https://github.com/Tencent/Hippy/commit/5048b0b772fc78c766fea5ca67fcb764bb0b3a32))
* **ios:** fix text input placeholder error ([83b6f48](https://github.com/Tencent/Hippy/commit/83b6f48da3bef9dbd35669badef8db040cf97f74))
* **ios:** fix view creation missing if view is created lazily ([b8747ac](https://github.com/Tencent/Hippy/commit/b8747acf62c913731c471422ad80c40fe6969aac))
* **ios:** fix viewpager component bug ([#564](https://github.com/Tencent/Hippy/issues/564)) ([56c481b](https://github.com/Tencent/Hippy/commit/56c481bda62a3268b2259430f54fa685de2eafcd))


### Features

* **android:** add gaussian blur property to image ([6c7290e](https://github.com/Tencent/Hippy/commit/6c7290e19bfa4ecda11b0dad00ef49e7b615f624))
* **android:** add local aar dependencies in demo ([227302c](https://github.com/Tencent/Hippy/commit/227302c5dea8d117213ac5b3461abd738ad9c796))
* **android:** change gaussian blur property type ([2733d1a](https://github.com/Tencent/Hippy/commit/2733d1a249ec0b6feae8530f803ba7e1111e8fe7))
* **android:** night mode support ([31ca5d1](https://github.com/Tencent/Hippy/commit/31ca5d1b1e371b846bc73d7790a248cd409d9e9f))
* **android:** support abi and v8 gradle setting ([8961799](https://github.com/Tencent/Hippy/commit/8961799439c890fc84ce24fc593697c3b4307a9e))
* **android:** support assemble by product flavors ([d0ecb6a](https://github.com/Tencent/Hippy/commit/d0ecb6a615c58c4e89648bb1fe41d8a5c1ea9e17))
* **android:** support register multi module name ([3060283](https://github.com/Tencent/Hippy/commit/30602839b598d2ff51b2260faab7a4551bf14ebc))
* **hippy-core:** export so symbol ([3f9d5cb](https://github.com/Tencent/Hippy/commit/3f9d5cb98079176f03874a6b6607d6fb43ed1b3a))





## [2.3.4](https://github.com/Tencent/Hippy/compare/2.3.3...2.3.4) (2021-01-25)


### Bug Fixes

* **android:** add debug and release argument for cmake ([fff9084](https://github.com/Tencent/Hippy/commit/fff90840b71d7267e4bb502de372502d7a4f3398))
* **android:** add support-annotations:28.0.0 ([46d5bf2](https://github.com/Tencent/Hippy/commit/46d5bf2e7a285c571187f8c34a9df2c1225ad42f))
* **ios:** block ui actions when bridge is invalide ([2581f3d](https://github.com/Tencent/Hippy/commit/2581f3d948760b51d31647f82b8588d29c91ceca))
* **ios:** fix error on drawing image with backgroundPositionX/Y ([a57240a](https://github.com/Tencent/Hippy/commit/a57240a044235e85cfbaefcd8ea77d791a044a28))
* **ios:** fix reference cycle in block ([3a77ebe](https://github.com/Tencent/Hippy/commit/3a77ebe1849b12522dd3d5e3bd4dd90d0909ee73))
* **ios:** no more tracking safa area for tableview ([df91cb4](https://github.com/Tencent/Hippy/commit/df91cb4ca1a399be4ec8705b5f962852e24c671a))


### Features

* **hippy-core:** add navigatorBarHeight ([71b5470](https://github.com/Tencent/Hippy/commit/71b5470475d71564275001f67600218d0569348b))





## [2.3.3](https://github.com/Tencent/Hippy/compare/2.3.2...2.3.3) (2021-01-20)


### Bug Fixes

* **android:** can not find cmake path ([720750a](https://github.com/Tencent/Hippy/commit/720750a2312614436f609c484f521a3c9eefe7ae))
* **ios:** fix image load error event ([d4099ee](https://github.com/Tencent/Hippy/commit/d4099eea71c2f9d4f4dc19c120edd25d78e46362))
* **ios:** fix network type judgment method ([#517](https://github.com/Tencent/Hippy/issues/517)) ([73daf87](https://github.com/Tencent/Hippy/commit/73daf87a9b7b418281296efd29493c16717d7835))


### Features

* **ios:** hippy now will send ‘destroyInstance’ event to JS ([b0ae490](https://github.com/Tencent/Hippy/commit/b0ae490d9ac13b9f79d6b0e95c7add02bf4ecb07))
* **ios:** optimize image load error message ([e20bed9](https://github.com/Tencent/Hippy/commit/e20bed987e86a5a184247196243867d3aa75b3c4))





## [2.3.2](https://github.com/Tencent/Hippy/compare/2.3.1...2.3.2) (2021-01-18)


### Bug Fixes

* **hippy-react:** fix appendChild error in react16 by key diff ([233e40c](https://github.com/Tencent/Hippy/commit/233e40c48511eee5745f189a6b9b58df1604a377))
* **ios:** fix crash due to a nil pointer of JSGlobalContextRef ([8aedca6](https://github.com/Tencent/Hippy/commit/8aedca638dc1ad97634a5b84e64f6a96e782df08))


### Features

* **hippy-react:** added demo key ([bd2201b](https://github.com/Tencent/Hippy/commit/bd2201b525c33642a67f11b9cd4e95c0c51159bc))





## [2.3.1](https://github.com/Tencent/Hippy/compare/2.3.0...2.3.1) (2021-01-13)


### Bug Fixes

* **ios:** fix bug that onPageScroll and onPageSelected invoke incorrect ([f86623d](https://github.com/Tencent/Hippy/commit/f86623d5e0d19194efe5a1fd0a2225e07c2d3cec))


### Features

* **core:** added destroyInstance event ([1dd9783](https://github.com/Tencent/Hippy/commit/1dd97839a98df0e4979672861b90e33651dcdfc8))
* **hippy-react:** added Text displayName ([f591206](https://github.com/Tencent/Hippy/commit/f5912066a56d3ebed8a5ec0cd96e8bedc46d792f))





# [2.3.0](https://github.com/Tencent/Hippy/compare/2.2.2...2.3.0) (2021-01-11)


### Bug Fixes

* **android:** revert RecyclerViewBase onTouchMove call ([2bbf339](https://github.com/Tencent/Hippy/commit/2bbf3399e481fdecf9a12c72bb69b444f6cee6de))
* **core:** fix exception rethrow bug ([60cf9cc](https://github.com/Tencent/Hippy/commit/60cf9ccea1098a9fb5b91c60f3259e378b52194a))
* **hippy-react-web:** correctly add px to number style values ([f774726](https://github.com/Tencent/Hippy/commit/f7747265d18565811e4346f6b342d23c26cf3827))
* **hippy-vue,hippy-react:** fixed listview appear & disappear event ([86c02fd](https://github.com/Tencent/Hippy/commit/86c02fde0e47ff8862e92a4892c768da78a63674))
* **ios:** fix a notification error with wrong object ([ae8fa7f](https://github.com/Tencent/Hippy/commit/ae8fa7ffcdecc0aed28f79d59174210d65cb82fd))
* **ios:** fix header file include error ([7786837](https://github.com/Tencent/Hippy/commit/77868370777043363f689fd5696463ad816044f7))
* **ios:** fix image show when image source did not change ([a67e81d](https://github.com/Tencent/Hippy/commit/a67e81d0b15c6d8e6836dc3813dfe8ec33cd31d3))
* **ios:** onPulling will occur on right number ([f79d99d](https://github.com/Tencent/Hippy/commit/f79d99d8bd49e9c4d106c99a929b7cd2f9d6b543))
* **ios:** remove an unused gesture ([7259e94](https://github.com/Tencent/Hippy/commit/7259e944e0b817b1a324680dd92a75461c5e9185))
* **ios:** remove check numberOfRows property ([269a75c](https://github.com/Tencent/Hippy/commit/269a75c2c7bf7e010d54c906ac09209e6cd5fc0e))


### Features

* **core:** add delloc event ([b11ae13](https://github.com/Tencent/Hippy/commit/b11ae136b6b6a62506f60697c83009078c2f28fb))
* **hippy-react:** forward Text ref ([5595dc1](https://github.com/Tencent/Hippy/commit/5595dc14d430c0289692358b47de3975f40945c2))
* **hippy-react-web:** added ScrollView scrollEnabled attribute ([efb1ea1](https://github.com/Tencent/Hippy/commit/efb1ea15dea6b16fe06222d604297339354733f1))
* **hippy-vue-demo,hippy-react-demo:** add dynamic import demo ([c4f357b](https://github.com/Tencent/Hippy/commit/c4f357b77d5ee5183f960afbebff7d0f7b1298c9))
* **hippy-vue,hippy-react:** added willAppear & willDisappear event ([d0eb0f1](https://github.com/Tencent/Hippy/commit/d0eb0f1aac4a13886fed6682ac08e2c082bd8448))
* **ios:** add appear event for cell in list view ([#496](https://github.com/Tencent/Hippy/issues/496)) ([2b9b653](https://github.com/Tencent/Hippy/commit/2b9b653d284041743c7915f0e016eea6dbe72d1e))





## [2.2.2](https://github.com/Tencent/Hippy/compare/2.2.1...2.2.2) (2021-01-04)


### Bug Fixes

* **hippy-react-web:** fixed image newProps src unRendered problem ([#472](https://github.com/Tencent/Hippy/issues/472)) ([86bd187](https://github.com/Tencent/Hippy/commit/86bd1874ff6ebb9f5ad97260e595b6e35b54cd29))
* **hippy-vue:** fix css rulesets concat error ([d0c900c](https://github.com/Tencent/Hippy/commit/d0c900c44be8c7759db8539a3c57be845471d227))
* **ios:** fix gif play error when using gif cache data ([32d0729](https://github.com/Tencent/Hippy/commit/32d07295e6d5efa5feebeeb9b587a6f607bdc081))


### Features

* **ios:** set a switch for red box show ([eb44a9c](https://github.com/Tencent/Hippy/commit/eb44a9c41386ec496b3d9f9da6aaade614fa2447))





## [2.2.1](https://github.com/Tencent/Hippy/tree/master/examples/hippy-vue-demo/compare/2.2.0...2.2.1) (2020-12-28)


### Bug Fixes

* **hippy-react-web:** fixed image onLoadEnd undefined ([#470](https://github.com/Tencent/Hippy/tree/master/examples/hippy-vue-demo/issues/470)) ([6e312b6](https://github.com/Tencent/Hippy/tree/master/examples/hippy-vue-demo/commit/6e312b6b623d3e53ff12e725003dc28a10a7e61f))





# [2.2.0](https://github.com/Tencent/Hippy/compare/2.1.6...2.2.0) (2020-12-25)


### Bug Fixes

* **android:** local image uri start with file:// ([34ba5cf](https://github.com/Tencent/Hippy/commit/34ba5cfc4085eaceb0d050affe726858223229c1))
* **core:** add jni code ([f5451fd](https://github.com/Tencent/Hippy/commit/f5451fd151acf8d33f6f3336e119aa9aeaa94b34))
* **hippy-core:** fixed network header default type ([#466](https://github.com/Tencent/Hippy/issues/466)) ([2879afc](https://github.com/Tencent/Hippy/commit/2879afcfbfc2aa87a515a3f74de40e6e1d909033))
* **hippy-react-web:** fixed api to web issues and some warnings ([#456](https://github.com/Tencent/Hippy/issues/456)) ([80efec7](https://github.com/Tencent/Hippy/commit/80efec7aca16cd5a16575bac6696c7f12cf04626))
* **hippy-react,hippy-vue:** fixed boxShadow annotation ([#460](https://github.com/Tencent/Hippy/issues/460)) ([fceb70e](https://github.com/Tencent/Hippy/commit/fceb70ee4803a978ea4c1b28fd33700b991dee42))


### Features

* **android:** support dynamic load ([67b0b3f](https://github.com/Tencent/Hippy/commit/67b0b3f9becb26fdcd729c05b2a940134eba58b7))
* **core:** add dynamic load ([20e2ed9](https://github.com/Tencent/Hippy/commit/20e2ed983d556897c9b9c2fcc7e6d8d858131070))
* **core:** update so ([5d6a25c](https://github.com/Tencent/Hippy/commit/5d6a25c2db8d6dd1987c851adc4836430291bda8))
* **ios:** add dynamic load ([50c427a](https://github.com/Tencent/Hippy/commit/50c427ab4361b7c5e3c0463965553dd2043af43c))
* **ios:** same businesses will share same memory in LocalStorage modul ([8ab4b3b](https://github.com/Tencent/Hippy/commit/8ab4b3b44885d44dba41ba3798cfe1bcfc948cb1))





## [2.1.6](https://github.com/Tencent/Hippy/compare/2.1.5...2.1.6) (2020-12-23)


### Bug Fixes

* **android:** add shadow offset props ([0f8eb08](https://github.com/Tencent/Hippy/commit/0f8eb084facd324b9298f218a2457445073ca648))
* **android:** draw shadow with border radius ([87a608a](https://github.com/Tencent/Hippy/commit/87a608ab744ac7ed7f0a5a44e27fd847d89100fd))
* **hippy-react:** fixed hairlineWidth NaN in ios ([82faee1](https://github.com/Tencent/Hippy/commit/82faee1f6693dce534ad97e18f4a42d9af1d2d9d))
* **hippy-vue,hippy-react:** compatible loadMore and endReached event ([#429](https://github.com/Tencent/Hippy/issues/429)) ([d992cbe](https://github.com/Tencent/Hippy/commit/d992cbefbd9a0f76bee70bf604df7d377a08e97c))
* **ios:** fix crash caused by a nil pScope ([2ae1349](https://github.com/Tencent/Hippy/commit/2ae134969cea5a0601a7e61d7c677ac4541dd050))
* **ios:** fix memory leaks in HippyBaseListViewCell ([#453](https://github.com/Tencent/Hippy/issues/453)) ([39d1760](https://github.com/Tencent/Hippy/commit/39d1760dd04e4fe75370632ef1c4106bc466f5e2))
* **ios:** hippy image loader will search the cache ([c13f4d1](https://github.com/Tencent/Hippy/commit/c13f4d14bc0677a5fd61228d1f2e4f59da0c822f))


### Features

* **demo:** perf demo dev debug experience ([#452](https://github.com/Tencent/Hippy/issues/452)) ([20fda8c](https://github.com/Tencent/Hippy/commit/20fda8c92b8bebb584b4636ea4685c18d5969215))
* **hippy-react:** added hippy-react boxShadow attr and demo ([#458](https://github.com/Tencent/Hippy/issues/458)) ([6fd6a34](https://github.com/Tencent/Hippy/commit/6fd6a342f7c0b7b6aa742eeee5c585e9e5a1d31b))
* **hippy-vue:** change boxShadowOffset standard format ([7be6617](https://github.com/Tencent/Hippy/commit/7be661783182ef6d2e024e260c462bc33dcba4e6))
* **hippy-vue:** vue css selectors support dynamic import ([#440](https://github.com/Tencent/Hippy/issues/440)) ([3baa571](https://github.com/Tencent/Hippy/commit/3baa57105df8f4e7a46d52d4334a88ee921c388d))
* **hippy-vue-example:** change dev cssloader path ([#451](https://github.com/Tencent/Hippy/issues/451)) ([795bf75](https://github.com/Tencent/Hippy/commit/795bf7588af20e740de23b0295f87b2bf69e8343))





## [2.1.5](https://github.com/Tencent/Hippy/compare/2.1.4...2.1.5) (2020-12-10)


### Bug Fixes

* **android:** correct text truncate ([58efa65](https://github.com/Tencent/Hippy/commit/58efa65fc47030639238547813b85b9a2f304df5))
* **android:** support customize debug port ([2b88947](https://github.com/Tencent/Hippy/commit/2b88947f1a6fc8a7c1097e8fe445ab64cc511f16))
* **hippy-react:** continue finding nodeId if stringref's stateNode is a ([#442](https://github.com/Tencent/Hippy/issues/442)) ([3860d3f](https://github.com/Tencent/Hippy/commit/3860d3ff3c36299b1f973dedbede83bcf94fa9ad))





## [2.1.4](https://github.com/Tencent/Hippy/compare/2.1.3...2.1.4) (2020-12-03)


### Bug Fixes

* **hippy-react:** fix pullHeader and pullFooter ([#420](https://github.com/Tencent/Hippy/issues/420)) ([abfc574](https://github.com/Tencent/Hippy/commit/abfc57401951acca4fb3fea72456784efcd4e926))
* **hippy-react-web:** suppress childContextTypes warning ([#431](https://github.com/Tencent/Hippy/issues/431)) ([4a7ba66](https://github.com/Tencent/Hippy/commit/4a7ba66fc7ada2ba0f4010ab49b4e24d1886bca2))
* **hippy-react,hippy-vue:** fixed setNativeProps demo ([#436](https://github.com/Tencent/Hippy/issues/436)) ([cf8be7c](https://github.com/Tencent/Hippy/commit/cf8be7cb19919bf5a1fe2bdd5372690ec98024da))


### Features

* **hippy-vue,hippy-react:** added setNativeProps on element ([#430](https://github.com/Tencent/Hippy/issues/430)) ([d1f7e21](https://github.com/Tencent/Hippy/commit/d1f7e216b5fef46ace0cf50803ad2940b429a0d6))
* **hippy-vue,hippy-react:** perf setNativeProps ([5cd1291](https://github.com/Tencent/Hippy/commit/5cd12910262ad3bb15d07c2dc974a829958a2b86))
* support isomorphic rendering ([#415](https://github.com/Tencent/Hippy/issues/415)) ([c131734](https://github.com/Tencent/Hippy/commit/c1317348bab9d38990decf7d39f0631d807ab9a5))





## [2.1.2](https://github.com/Tencent/Hippy/compare/2.1.1...2.1.2) (2020-11-23)


### Bug Fixes

* **hippy-debug-server:** fixed the way the server read from file ([#405](https://github.com/Tencent/Hippy/issues/405)) ([fa16ee8](https://github.com/Tencent/Hippy/commit/fa16ee85a31cfbd05c91e33a8b3f4029a15936ac))
* **hippy-debug-server:** response 404 when file not found ([#410](https://github.com/Tencent/Hippy/issues/410)) ([8b161a8](https://github.com/Tencent/Hippy/commit/8b161a883d8506015cb7c0f0c41cc4fc2022a89e))
* **ios:** change way to get bridge property ([1f7a843](https://github.com/Tencent/Hippy/commit/1f7a843e0d3d84ee132ab6fb52d75cc651f16c5a))
* **ios:** fix bugs that some js exceptions may not be caught ([629fa22](https://github.com/Tencent/Hippy/commit/629fa224a889e3aba3a2bb383286983daf834a4b))
* **ios:** fix loop count property ([7a3f093](https://github.com/Tencent/Hippy/commit/7a3f093a3cd96038d501e13d67af38e69d65d8f4))
* **ios:** fix loop count property ([6d44bf4](https://github.com/Tencent/Hippy/commit/6d44bf469937e18873aa8175a9bd8446b106371d))
* **ios:** fix multiple thread competion ([f281bd1](https://github.com/Tencent/Hippy/commit/f281bd17ef7437e634e3656a0498aacc33480b67))
* **ios:** fix the bug that get wrong _bridge value ([b4132c7](https://github.com/Tencent/Hippy/commit/b4132c70155571bf993695308b05d35b96a01393))
* **ios:** fix threads competion bugs ([258a480](https://github.com/Tencent/Hippy/commit/258a4804fa16ff28731cd066ff22b71886f05d3a))
* **ios:** have to consider an exception for QB ([9d4f724](https://github.com/Tencent/Hippy/commit/9d4f724042f67ca0ee82aeffda7f0ffcb34b1c50))
* **ios:** jscontextref held by jscontextgroupref cannot be deallocated ([e85e0a3](https://github.com/Tencent/Hippy/commit/e85e0a31986965152e59c1bc2f334c9a5ed16aba))
* **ios:** now modal component property will be set corrently ([0fbc5ad](https://github.com/Tencent/Hippy/commit/0fbc5ad8f95a4babf09622ef47518b05e8b51733))
* **ios:** use isEqualToString for NSString comparison ([fef7971](https://github.com/Tencent/Hippy/commit/fef79713b20613735d99978bb8909c3f07429a9d))


### Features

* **hippy-debug-server:** modify debug server to support multiple files ([#411](https://github.com/Tencent/Hippy/issues/411)) ([508ec9f](https://github.com/Tencent/Hippy/commit/508ec9f2f11070b1616cf155a330fb538d9c23ff))
* **hippy-vue:** added iOS12 statusBarHeight ([d33e993](https://github.com/Tencent/Hippy/commit/d33e993b72b3627e3fd73e5f5e08d8c34d4ee23d))
* **hippy-vue, hippy-react:** changeTryConvertNumberCompatibility ([714faaf](https://github.com/Tencent/Hippy/commit/714faaf11988659b450a3276342597b7ed095a17))
* **ios:** add method for HippyBridge ([a24dbfd](https://github.com/Tencent/Hippy/commit/a24dbfd17f15f05e3bc20476b0210d2f45f750b6))
* **ios:** check necessity of reloading image for HippyImageView ([34ce4eb](https://github.com/Tencent/Hippy/commit/34ce4eb72a4479cd6cba1edadaf89fd894af0905))
* **ios:** fix incorrect of image cache ([c00c344](https://github.com/Tencent/Hippy/commit/c00c34467742d67d062e6a920149405cb4bc9b44))
* **ios:** increase error infomation propery ([f2f24e8](https://github.com/Tencent/Hippy/commit/f2f24e8e9f7cc679eb356515b2c60e39b30721a5))
* **ios:** modify pod config file ([d889fd4](https://github.com/Tencent/Hippy/commit/d889fd4a86a11982dd09598d7f93307780d704bd))
* **ios:** perfect error function ([dbfefd5](https://github.com/Tencent/Hippy/commit/dbfefd5936501556063218e8759b771a1cfe0020))
* **ios:** set predrawing no for animated image ([d80b04a](https://github.com/Tencent/Hippy/commit/d80b04ae1ba377fc98411ed15a91805e90efea05))





## [2.1.1](https://github.com/Tencent/Hippy/compare/2.1.0...2.1.1) (2020-11-11)


### Bug Fixes

* **hippy-vue:** fixed focus event support for div ([#387](https://github.com/Tencent/Hippy/issues/387)) ([21d8b58](https://github.com/Tencent/Hippy/commit/21d8b58946f546bc2313c163818f525b0da8ef9a))
* **hippy-vue:** fixed tryConvertNumber bug & some compatible issue ([ba8836d](https://github.com/Tencent/Hippy/commit/ba8836d9b3c3461f013d325c0e86c84233e3ede6))




# [2.1.0](https://github.com/Tencent/Hippy/compare/2.0.3...2.1.0) (2020-10-29)


### Bug Fixes

* **android:** avoid null pointer before bouncing RefreshWrapper ([43c2fb5](https://github.com/Tencent/Hippy/commit/43c2fb56360c2b4471a6c0cbefb1bcec94e87edd))
* **android:** change hippy.jar storage path ([161cb38](https://github.com/Tencent/Hippy/commit/161cb38663b036783ec64f6e45fa8a957500b906))
* **android:** change style of font size invalid ([a0b534f](https://github.com/Tencent/Hippy/commit/a0b534f17118a464ff8e1328c1dba23ca2034b3a))
* **android:** checkUpdateDimension window height==width problem for QB ([1011371](https://github.com/Tencent/Hippy/commit/1011371303a6233a97604f1d8d321f531179a9fd))
* **android:** compile problem ([631c9d3](https://github.com/Tencent/Hippy/commit/631c9d3e90039d09186d0dcfe647c78476d0140d))
* **android:** context holder memory leak ([b810bc1](https://github.com/Tencent/Hippy/commit/b810bc16f293bd18fd8f23ae3b66a278a54cb698))
* **android:** correct list view onEndReached to onLoadMore ([eaa7e5f](https://github.com/Tencent/Hippy/commit/eaa7e5faa141cd0d703e473d8f141b7b7adbed7d))
* **android:** dispatchFunction with promise for textinput getValue ([f886e0b](https://github.com/Tencent/Hippy/commit/f886e0b15297caaf86cb8147e7353a83d4743f7a))
* **android:** fetchImage param should support different type ([497ba71](https://github.com/Tencent/Hippy/commit/497ba7155e081774de507d18076b7e6fb0fd7f98))
* **android:** fix support-ui code style ([04c9959](https://github.com/Tencent/Hippy/commit/04c99593742f614256f95b0b83c5a97918096ef2))
* **android:** get listView frame size function ([74e2769](https://github.com/Tencent/Hippy/commit/74e2769399d29b02c9446af9b999dcb0c97f2887))
* **android:** imageview will flash when list loadmore ([#228](https://github.com/Tencent/Hippy/issues/228)) ([366247a](https://github.com/Tencent/Hippy/commit/366247abcb89772ce11c539a3d66fdfe124131c1))
* **android:** list view flash when exceed 7 item in screen ([0109fe5](https://github.com/Tencent/Hippy/commit/0109fe521bf888ed65f06788af6ab6bd34d67886))
* **android:** list view init position incorrect when contain pull header ([c6eb90d](https://github.com/Tencent/Hippy/commit/c6eb90de9f5aea4e5140f3bd01cff62338f3a7ed))
* **android:** list view item reuse tintcolor invalid problem ([7270141](https://github.com/Tencent/Hippy/commit/727014141e358a0ddd7c2f3d21b0372367939444))
* **android:** make ListViewItem as same type props with iOS ([1ae5b54](https://github.com/Tencent/Hippy/commit/1ae5b54490e3659df6b4355708df70b274936d0d))
* **android:** memory leak suspect ([a9ef48d](https://github.com/Tencent/Hippy/commit/a9ef48df1a6a18b8fe2cee8f219cf65c02525d49))
* **android:** modal view crash when activity destroyed ([e73b656](https://github.com/Tencent/Hippy/commit/e73b656ed693d5b4fef56e355fcec01a1d909fbc))
* **android:** module load listener memory leak ([55a084f](https://github.com/Tencent/Hippy/commit/55a084fe2cc1c1fdb3698621ee5edabb52a89e08))
* **android:** optimize view hippmap tag impl ([f433ec4](https://github.com/Tencent/Hippy/commit/f433ec4246d2511c8a29a7611b5380e1067dc3e3))
* **android:** pic not display when adapter call back file path ([7620d7a](https://github.com/Tencent/Hippy/commit/7620d7adac330574cf13b52816f7694f1633c789))
* **android:** revert build gradle publish config ([b2c67e3](https://github.com/Tencent/Hippy/commit/b2c67e3658b849582528c759066cfc62826342ba))
* **android:** revert fetch image optimize in support ui ([416a6e2](https://github.com/Tencent/Hippy/commit/416a6e2c1c3396e41eea86c6ecd32a3429044c28))
* **android:** set engine context null may case crash ([10146f0](https://github.com/Tencent/Hippy/commit/10146f0119e6f8922942c1e27a81e08dc18cc918))
* **android:** support ui fetch image and react bug fix ([d829ed4](https://github.com/Tencent/Hippy/commit/d829ed48c2cb8001f3c19f8937863d71536ab76d))
* **android:** temporary remove example third party adapter ([6d148ec](https://github.com/Tencent/Hippy/commit/6d148ecc091dfc06af0bf0cf36e130e2b5a5fb07))
* **android:** tkd list view loadMoreFinish incorrect ([6659a07](https://github.com/Tencent/Hippy/commit/6659a078666ef6f1d3908d1e276db9e289a2d9ef))
* **android-viola:** modify maven configuration ([7558e6a](https://github.com/Tencent/Hippy/commit/7558e6a34d355f153346b5263e2c6e446a3cdde5))
* **android-viola:** modify maven configuration for 0.1.2 ([b7da1f0](https://github.com/Tencent/Hippy/commit/b7da1f0e6fad313a40dd9e72882b97c2d577a3f0))
* **doc:** add a new attribute to listView ([8232db6](https://github.com/Tencent/Hippy/commit/8232db658217717a9288bedfbc01f07ea2c979d8))
* **hippy-react:** fix hippy-react animationSet destroy problem ([#382](https://github.com/Tencent/Hippy/issues/382)) ([3c66ca6](https://github.com/Tencent/Hippy/commit/3c66ca676d8f4fa3bc852492d24e533c617b252d))
* **hippy-react:** removed unncessary Object.values() ([8a68d44](https://github.com/Tencent/Hippy/commit/8a68d44d7c7bd439b2be0badc542e9224685c76f))
* **hippy-react:** restore the ListView type props be number ([#367](https://github.com/Tencent/Hippy/issues/367)) ([231ec5a](https://github.com/Tencent/Hippy/commit/231ec5a37b41eb778b649e079ec5d6bbe712fb8f)), closes [/github.com/Tencent/Hippy/commit/9de74e331b797c2137b1d0e3d08cd0dde0ee821a#diff-ccaf44058906717491bd079958ea5684a93acaa5d726e22cb34c0a6c82c79](https://github.com//github.com/Tencent/Hippy/commit/9de74e331b797c2137b1d0e3d08cd0dde0ee821a/issues/diff-ccaf44058906717491bd079958ea5684a93acaa5d726e22cb34c0a6c82c79)
* **hippy-vue:** fix hippy-vue transform multi-animation not working ([84bd58b](https://github.com/Tencent/Hippy/commit/84bd58be840ea3f5ddd9d387e92b5a084387e9d1))
* **ios:** cancel previous custom image loading when a new request come ([3aef246](https://github.com/Tencent/Hippy/commit/3aef2466d5b52ad7daeeba0acdb072bf356b055c))
* **ios:** clear resources before Engine destroy ([fe6e080](https://github.com/Tencent/Hippy/commit/fe6e080754c2112d8043fa5cdf719be9c16fc50c))
* **ios:** code format, add a break to match static analysis ([61a43db](https://github.com/Tencent/Hippy/commit/61a43db3662d0090c34db8f6d8db09ba49c8ff8e))
* **ios:** fix a potential crash ([6239592](https://github.com/Tencent/Hippy/commit/623959298ebb3a58ae14a77223095583e89bdcc6))
* **ios:** fix animated image refresh bug ([8d5e306](https://github.com/Tencent/Hippy/commit/8d5e306e467da1296fe4281cd7cbfe08c3c374ba))
* **ios:** fix animated images check ([eb85032](https://github.com/Tencent/Hippy/commit/eb85032506e1418d6b175b31a227ba653a4956b6))
* **ios:** fix border radius bug ([bbb971f](https://github.com/Tencent/Hippy/commit/bbb971f9271c87e1f9bc2b83951e2fb856a611ba))
* **ios:** fix box shadow propery ([e816c31](https://github.com/Tencent/Hippy/commit/e816c31fbe5524e03dac04d3188831f604136d55))
* **ios:** fix bugs that onInterceptTouchEvent not work ([d615374](https://github.com/Tencent/Hippy/commit/d615374c7e445961e2a299801fee8c45a46ed73e))
* **ios:** fix dev window height on iphoneX ([7e5df6e](https://github.com/Tencent/Hippy/commit/7e5df6ebbf4681a1a0922a2df43ecce8c3d1e200))
* **ios:** fix footer pull method name ([bef9a19](https://github.com/Tencent/Hippy/commit/bef9a197eb51e953b8cf890408549809673e384c))
* **ios:** fix HippyTiming crash ([08fae32](https://github.com/Tencent/Hippy/commit/08fae32f4f3f73c793e6cc6a18cfecf604870175))
* **ios:** fix imageload failed message ([6dfe123](https://github.com/Tencent/Hippy/commit/6dfe123de06b738bdd7f6ef9b08e5b1e08baf56c))
* **ios:** fix list item view type and onEndReach event not trigger ([46bad5d](https://github.com/Tencent/Hippy/commit/46bad5dd842eff897ec87b33ff526b704d2061dc))
* **ios:** fix measureInWindow bug ([fceb0e7](https://github.com/Tencent/Hippy/commit/fceb0e770ffddf395090c79a9b75949c4bd8035c))
* **ios:** fix measureInWindow method,add measureInAppWindow ([e66cab1](https://github.com/Tencent/Hippy/commit/e66cab125521535c9c709394cea7785cb0e5e6a2))
* **ios:** fix touch handler bugs ([d706657](https://github.com/Tencent/Hippy/commit/d7066570f9b69e73d3e5717984eb644e15061ac4))
* **ios:** fix UIFont constructor crash on iOS14 ([be1ce5d](https://github.com/Tencent/Hippy/commit/be1ce5d0fca41f35647804aa0a3d3a8d2ee54505))
* **ios:** fix websocket bug ([f45a9bb](https://github.com/Tencent/Hippy/commit/f45a9bba7bd0a92f0c733d596e37973af059eb6b))
* **ios:** fix websocket function ([987b08c](https://github.com/Tencent/Hippy/commit/987b08c729411ba47b24cca3feff4f0d00260e5b))
* **ios:** fix websocket module function.remove debug code ([8bb3b37](https://github.com/Tencent/Hippy/commit/8bb3b37c6677ffb7461a684e672ee23ae6fe56a4))
* **ios:** set default image need not update imageview ([53a1eb2](https://github.com/Tencent/Hippy/commit/53a1eb24e7f5f0246fcc07bdb1f12c3d67741f9b))
* **ios:** support default font family name ([b97ca96](https://github.com/Tencent/Hippy/commit/b97ca9642ae04286a28f90051af9598060c7c2de))
* **ios:** update props without shadowview ([52f8295](https://github.com/Tencent/Hippy/commit/52f829513638db331eb0108961fb15ff218aff01))
* **swiper:** fixed props passing ([3081d69](https://github.com/Tencent/Hippy/commit/3081d6999fe7ce7556798d41caaa5bdee5907fb3))
* **vue:** fixed remove style issue ([#329](https://github.com/Tencent/Hippy/issues/329)) ([33f2f7d](https://github.com/Tencent/Hippy/commit/33f2f7d3a1518e70471a060b930b3372d6b49c99))


### Features

* **android:** add cell exposure event support ([fd9c464](https://github.com/Tencent/Hippy/commit/fd9c46427f33887e4067750a48ed5bdf69d15018))
* **android:** add exposure event enabled control ([c2f6307](https://github.com/Tencent/Hippy/commit/c2f6307303911cab699734097028aeb8571aadbe))
* **android:** add listView event support 1 ([1e85d9d](https://github.com/Tencent/Hippy/commit/1e85d9de547c5dba83925825cbcefd3d7e9c7ce9))
* **android:** add listView event support 2 ([a3712b2](https://github.com/Tencent/Hippy/commit/a3712b2d9ca25aac114de458a13661a6b81333fd))
* **android:** add listView horizontal support ([cf6e439](https://github.com/Tencent/Hippy/commit/cf6e4396cbbb9aca270fd3aa31283dd0508b0ff2))
* **android:** add page-slider and list view direction prop support ([14ae0a6](https://github.com/Tencent/Hippy/commit/14ae0a6e2fba7b824456a26f4ec830074b0b8798))
* **android:** add page-slider and page support ([826369f](https://github.com/Tencent/Hippy/commit/826369f90d7883dbdebb667f79f22a5ea464cd87))
* **android:** add pullHeader and pullFooter event support ([eeaac37](https://github.com/Tencent/Hippy/commit/eeaac37b2f707de137e1fc4cc1ad462bb4b95134))
* **android:** add pullHeader and pullFooter view support ([f6e432f](https://github.com/Tencent/Hippy/commit/f6e432f828f239a59bac596f071a6ab32285057a))
* **android:** add tdk feature step 1 ([dbc60c4](https://github.com/Tencent/Hippy/commit/dbc60c4d673e31dfe2f0962ffdeef2fa31f510a4))
* **android:** add view hippymap tag support ([799305a](https://github.com/Tencent/Hippy/commit/799305ac3b61ecbe4b4de2f8ef77655ee5190725))
* **android:** optimize list view item exposure impl ([5e09690](https://github.com/Tencent/Hippy/commit/5e09690d43ff751da6208095b459f25ef056a65d))
* **android:** support box-shadow feature ([3dbbe85](https://github.com/Tencent/Hippy/commit/3dbbe85d74ea2ecad59acb7c83ce54f08e051aed))
* **core:** catch hippy-base exception ([#353](https://github.com/Tencent/Hippy/issues/353)) ([0601446](https://github.com/Tencent/Hippy/commit/0601446dc7a0da61c7c73ed1e86c3816fb960b0d))
* **core:** refactor core ([f996af3](https://github.com/Tencent/Hippy/commit/f996af33379f41eefb9a3864756eb1861f70bbf2))
* **hippy-react:** add new method measureInAppWindow ([e25bb67](https://github.com/Tencent/Hippy/commit/e25bb676942b89fbb57601d7c4ac2c9ce8ec175f))
* **hippy-react:** added PullHeader and PullFooter components support ([2fcdee9](https://github.com/Tencent/Hippy/commit/2fcdee9b3ef290f40a25321c978a0c232299b06a))
* **hippy-vue:** add new method measureInAppWindow ([e6348a2](https://github.com/Tencent/Hippy/commit/e6348a2fa31ea61fcfda66151c15830871f47ab6))
* **hippy-vue:** added pull-header and pull-footer components support ([3fbc862](https://github.com/Tencent/Hippy/commit/3fbc86230eef085fe1e33efc53c8507bf3598233))
* **hippy-vue:** added the callback execution before $mount in $start ([1a1cc3f](https://github.com/Tencent/Hippy/commit/1a1cc3fb5ee92a3dd704765bc628530f9f146c8b))
* **hippy-vue:** box-shadow style support ([0604461](https://github.com/Tencent/Hippy/commit/06044610f85f891d52d28439b3a48554c8db6487))
* **hippy-vue:** export parseColor api for HippyVue ([a354c94](https://github.com/Tencent/Hippy/commit/a354c94ede4542bb9111c030e088a70f617ca0c7))
* **ios:** add custom viewcontroller transition animation ([9931ca4](https://github.com/Tencent/Hippy/commit/9931ca42bec513bfc01c3106f24939800aca500d))
* **ios:** add image provider protocol ([#349](https://github.com/Tencent/Hippy/issues/349)) ([d7ff14f](https://github.com/Tencent/Hippy/commit/d7ff14ff1376c5d10bfeb37d99adcb259c761008))
* **ios:** add listitemview appear and disappear event ([dbdc70a](https://github.com/Tencent/Hippy/commit/dbdc70adbbaec4d84e94d18d8c956353588c388a))
* **ios:** add pull header and pull footer components ([ac79c90](https://github.com/Tencent/Hippy/commit/ac79c9018cfb80a71a99b234055021e221f3ecaf))
* **ios:** change hippyDeepCopyProtocol method name ([beb417e](https://github.com/Tencent/Hippy/commit/beb417e9db6a1079986494f6a7e814668cfe612f))
* **ios:** expose image method public ([18307b9](https://github.com/Tencent/Hippy/commit/18307b90523e54d45b565c438bbad3b5a1a1ce61))
* **ios:** expose some properties and methods for Modal component ([cd60af2](https://github.com/Tencent/Hippy/commit/cd60af221a442a6855ab44e90769c9ff1c1ee1c9))
* **ios:** fix viewpager error ([c0c7bcc](https://github.com/Tencent/Hippy/commit/c0c7bcc271d1ab5d75092fecb7119f13963d4766))
* **ios:** no exception thrown in release mode in HippyFatal method ([049f57e](https://github.com/Tencent/Hippy/commit/049f57e99f4826e70c3fef26e58760c78b9d38bd))
* 适配core代码，新增EngineMapper文件 ([d4487e2](https://github.com/Tencent/Hippy/commit/d4487e2b6200e0befd56bf8d66b2d62e33ad7365))
* **ios:** hippyImageView not support all animated image ([c864c34](https://github.com/Tencent/Hippy/commit/c864c343322f71c130f9168d6bc9542c97aaa2eb))
* **ios:** implemention of refresh method ([d237501](https://github.com/Tencent/Hippy/commit/d237501961e2a893c89ae711ee23ca945805525a))
* **ios:** optimize animated image logic ([c71e09d](https://github.com/Tencent/Hippy/commit/c71e09d90b8702a674bffad30cfcb539aaf5004a))
* **ios:** remove macro of scroll delegate method ([1ad5d26](https://github.com/Tencent/Hippy/commit/1ad5d26ef81ca672bec63f431f20535e56362797))
* **ios:** support auto spacing feature ([dfffe2b](https://github.com/Tencent/Hippy/commit/dfffe2b423a608997873ad862729e0ac09a9078a))



## [2.0.3](https://github.com/Tencent/Hippy/compare/2.0.2...2.0.3) (2020-04-23)


### Bug Fixes

* **android:** change style of font size invalid ([a538c24](https://github.com/Tencent/Hippy/commit/a538c24bec38790d6e1e832bfc57e97832af64dc))
* **android:** input can not auto scroll when reach max length ([a9f2355](https://github.com/Tencent/Hippy/commit/a9f23556c44dbe49c90003196cda0eb898f70757))
* **android:** input can not auto scroll when reach max length ([8b25757](https://github.com/Tencent/Hippy/commit/8b25757ab67e450c9a518c53feca0fac4107096a))
* **android:** update arm64 so and should compile alone ([ee65c8e](https://github.com/Tencent/Hippy/commit/ee65c8e743d4d8d72a7bcdf7d516b711e7c9df98))
* **core:** drop Object.entries() for lower iOS compatible ([a3aa0b8](https://github.com/Tencent/Hippy/commit/a3aa0b8514e15f84d590cf0667cb15de2db7ccbd))
* **hippy-react:** drop Object.entries() for lower iOS compatible ([d76b074](https://github.com/Tencent/Hippy/commit/d76b074b7ed2536422be6052c56165be83b341c2))
* **hippy-react-web:** scroll-view style should be called by formatWebS… ([#233](https://github.com/Tencent/Hippy/issues/233)) ([9db12a4](https://github.com/Tencent/Hippy/commit/9db12a4fff59908c48f5547ddeb6deba68903af8))
* **ios:** fix bug that textinput does not support chinese input ([230c6a6](https://github.com/Tencent/Hippy/commit/230c6a60bfd58574408d66837b6bac1764e4487e))
* **ios:** fix extra modules provider bug ([9ba10c9](https://github.com/Tencent/Hippy/commit/9ba10c97b6eb2dfcde0d7e86cc0048f02b8344fe))
* **ios:** fix some bugs ([ce76475](https://github.com/Tencent/Hippy/commit/ce76475bc2c2904000945ecdc73a8f9ffbead8d8))
* **ios:** fixed TextInput blur and focus event trigger when click ([61045d8](https://github.com/Tencent/Hippy/commit/61045d83dae20cf8969661b3fe91fc0329ecfc02))
* **ios:** fixed the value of _JSMethodName in HippyModuleMethod ([4d5fde8](https://github.com/Tencent/Hippy/commit/4d5fde8cce791bc929ed0be65e8ec5a99e71ac23))
* **ios:** reset animated image when reset image source ([9060cfc](https://github.com/Tencent/Hippy/commit/9060cfc54076bfab10a50bf9091a94cc8596cc08))
* **react-web:** annimation-set support opacity ([b29e92c](https://github.com/Tencent/Hippy/commit/b29e92cac95f444925a99dcae82cdf5bcbd47f2a))


### Features

* **hippy-react:** merge createNode operation ([#200](https://github.com/Tencent/Hippy/issues/200)) ([04d77a0](https://github.com/Tencent/Hippy/commit/04d77a074c5d43cbf4bfa0cc40c513167314addc))
* **hippy-react-web:** added default export for hippy-react web ([62cbdb0](https://github.com/Tencent/Hippy/commit/62cbdb0cb7d65c989439e1d7ffb0a5fa1143eddd))
* **hippy-vue:** make beforeStyleLoad hooks applied in runtime ([2fc49cf](https://github.com/Tencent/Hippy/commit/2fc49cf819c32038b780569a8d278a865e438703))
* **ios:** mount all custom objects at __HIPPYNATIVEGLOBAL__ ([9430138](https://github.com/Tencent/Hippy/commit/9430138ae6ad8d71f91f0089f78114ccc00bde85))
* **ios:** update the way custom  objects before execute JS ([e2ee31c](https://github.com/Tencent/Hippy/commit/e2ee31c7449a9b40c06a684b528765524a7dcada))
* **ios:** user can mount custom objects before execute JS ([0df5b43](https://github.com/Tencent/Hippy/commit/0df5b43a111e301635d81ba58ab7114d8a11b331))
* **react-demo:** add router ([c759cbe](https://github.com/Tencent/Hippy/commit/c759cbe400564ccaee239e84e8e52b82d58eaf89))
* **react-web:** support layout event ([fafa65c](https://github.com/Tencent/Hippy/commit/fafa65c2fc6f23cc717f35a163cdf4d7b43ff6a5))





## [2.0.2](https://github.com/Tencent/Hippy/compare/2.0.1...2.0.2) (2020-03-18)


### Bug Fixes

* **android:** compile problem ([df4398d](https://github.com/Tencent/Hippy/commit/df4398da08d3c1d0c67e23912d42d955335705b6))
* **android:** fix keyboard hiding when blur in endEditing after focus ([22e8602](https://github.com/Tencent/Hippy/commit/22e86021a2153b3c8249a3a82e825f8361a7ac1c))
* **android:** textinput focus and blur problem ([#161](https://github.com/Tencent/Hippy/issues/161)) ([44daa93](https://github.com/Tencent/Hippy/commit/44daa93ee9ba45aa3c3a41ff30c25d22f1ab4224))
* **android:** treat Parcelable[] as Bundle[] ([a2ffb28](https://github.com/Tencent/Hippy/commit/a2ffb2813f8744fdb15dcda13eb13b887259840d))
* **example:** split js bundle with webpack dllplugin ([c8ac36d](https://github.com/Tencent/Hippy/commit/c8ac36d081fd0f41d92d566576443febdf194c8f))
* **hippy-react:** callUIFunction supports passing  as targetNode ([f7c8391](https://github.com/Tencent/Hippy/commit/f7c83911622140db9f3f5ac9eba44aefe44cd4ce))
* **hippy-react:** change the NODE_ENV to 'development' ([2585bc5](https://github.com/Tencent/Hippy/commit/2585bc5f3f28816c7ff1f4bdf210011508e7d2e8))
* **hippy-react:** text component text repeated rendering ([96e278d](https://github.com/Tencent/Hippy/commit/96e278d33c3bd18cdec6c839cc5454c5c3479224))
* **hippy-react:** text nest ([da5ca3b](https://github.com/Tencent/Hippy/commit/da5ca3b45cad28659bf0c6bacc90a1a64658d906))
* **hippy-react:** text-input style ([a9fa8d1](https://github.com/Tencent/Hippy/commit/a9fa8d1e896c5d5ee62c8ef09d6b32de85124618))
* **hippy-react:** ui operation merge ([9b4f77d](https://github.com/Tencent/Hippy/commit/9b4f77dfa54a0747efccec3beee6db170c3848cd))
* **hippy-vue:** add TypeSelector test ([38f08ef](https://github.com/Tencent/Hippy/commit/38f08ef4180fa08781492ea80f3dcfbc3ad37036))
* **hippy-vue:** fix css selectors TypeSelector match ([de98e8a](https://github.com/Tencent/Hippy/commit/de98e8a560ee771d4f10fcbd3642afccfc92e70e))
* **hippy-vue:** fix wrong preSibling of childNode's nexSibling ([6e76d5e](https://github.com/Tencent/Hippy/commit/6e76d5e1ead0a1d359ab0ec3d25d94c2ffed792b))
* **hippy-vue:** fixed css selectors TypeSelector match ([adddcea](https://github.com/Tencent/Hippy/commit/adddcea319c816d49deed0b2893e6ee82c203648))
* **ios:** fix crash caused by incorrect creation of UIFont ([0764fdc](https://github.com/Tencent/Hippy/commit/0764fdc5b531edb9ac868909194ecee79c44073a))
* **ios:** fix issue of invalidation of customized font ([3c632dc](https://github.com/Tencent/Hippy/commit/3c632dca651b91e36ef92219d0c7b6a38a8a54b8))
* **ios:** fix return type of TextInput.getValue method ([5db3dac](https://github.com/Tencent/Hippy/commit/5db3dac6a69973196b42d90c7eed0a745fb5a130))
* **ios:** fix some bugs ([a329d40](https://github.com/Tencent/Hippy/commit/a329d4060ed78dddd2dfe72dea5756c94ba14c1c))
* **react-web:** image callback error ([1776634](https://github.com/Tencent/Hippy/commit/1776634552356d32dcb51e2e98ce1e797788dd3c))
* **react-web:** listView not work in dev mode ([44a539f](https://github.com/Tencent/Hippy/commit/44a539fc978e70294b0f30707912aa80dc6652d3))
* **react-web:** updateAnimation support zero ([0ec1dc8](https://github.com/Tencent/Hippy/commit/0ec1dc87aee14660954799c4a89a6d4584a4afe9))
* **vue:** setStyle px unit determine ([8379d53](https://github.com/Tencent/Hippy/commit/8379d53e4b275dda8243b1869eded475a0113373))


### Features

* **android:** add bintray jcenter upload support ([11900b0](https://github.com/Tencent/Hippy/commit/11900b0f9330818c9dfc99d813bfef7d301683cf))
* **ios:** add shadow feature for HippyView ([f111951](https://github.com/Tencent/Hippy/commit/f111951baedc153a229a251fa8fb7bd27f1b52eb))
* **react-web:** support multiple instance ([c5fb93b](https://github.com/Tencent/Hippy/commit/c5fb93be6e40f14f731a6f5dd750254bc049838d))
* **react-web:** text support clip mode ([516fa98](https://github.com/Tencent/Hippy/commit/516fa988c6e616c3a49790d83b06ff4d6ff6d9fd))
* **vue:** added disabled props to input tag ([47facd4](https://github.com/Tencent/Hippy/commit/47facd4584b1361a760fd62162b0d4f9384ee673))
* **vue-native-components:** added stateChanged event handler to swiper ([71760cc](https://github.com/Tencent/Hippy/commit/71760cccf15a819c644efaa1e084a96fcc4e856e))
