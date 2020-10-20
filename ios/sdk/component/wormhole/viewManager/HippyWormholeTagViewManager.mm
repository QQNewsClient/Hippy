//
//  HippyWormholeTagViewManager.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeTagViewManager.h"
#import "HippyWormholeTagView.h"
#import "HippyWormholeTagShadowView.h"

@implementation HippyWormholeTagViewManager

HIPPY_EXPORT_MODULE(TKDWormhole)

HIPPY_EXPORT_VIEW_PROPERTY(params, NSDictionary);

- (UIView *)view {
    return [HippyWormholeTagView new];
}

- (HippyShadowView *)shadowView{
    return [HippyWormholeTagShadowView new];
}

HIPPY_EXPORT_METHOD(sendEventToWormholeView:(nonnull NSNumber *)reactTag message:(NSDictionary *)message)
{
    [self.bridge.uiManager addUIBlock:^(HippyUIManager *uiManager, NSDictionary<NSNumber *,__kindof UIView *> *viewRegistry) {
        HippyWormholeTagView *view = viewRegistry[reactTag];
        [view sendEventToWormholeView:message];
    }];
}

@end
