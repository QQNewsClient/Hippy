//
//  NSObject+WhormholdViewModel.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "NSObject+WhormholdViewModel.h"
#import "objc/runtime.h"
#import "HippyWormholeViewModel.h"

@implementation NSObject (WhormholdViewModel)

- (HippyWormholeViewModel *)wv_wormholeViewModel{
    HippyWormholeViewModel * model = objc_getAssociatedObject(self, @selector(wv_wormholeViewModel));
    return model;
}

- (void)setWv_wormholeViewModel:(HippyWormholeViewModel *)wv_wormholeViewModel{
    objc_setAssociatedObject(self, @selector(wv_wormholeViewModel), wv_wormholeViewModel, OBJC_ASSOCIATION_RETAIN);
}

@end
