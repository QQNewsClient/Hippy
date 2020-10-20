//
//  HippyWormholeTagView.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeTagView.h"
#import "HippyBridge.h"
#import "HippyWormholeProtocol.h"
#import "HippyBridge.h"
#import "HippyEventDispatcher.h"
#import "HippyWormholeViewModel.h"
#import "NSObject+WhormholdViewModel.h"
#import "HippyUIManager.h"

@interface HippyWormholeTagView()
@end

@implementation HippyWormholeTagView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
    }
    return self;
}


- (NSString *)description{
    NSString *appendStr = nil;
    if (_params.wv_wormholeViewModel)
    {
        appendStr = _params.wv_wormholeViewModel.wormholeId;
    }
    return [NSString stringWithFormat:@"%@; wormholeId:%@", [super description], appendStr];
}

- (void)setParams:(NSDictionary *)params {
    if (_params != params)
    {
        _params = params;
        HippyWormholeViewModel * rViewModel = params.wv_wormholeViewModel;
        assert(rViewModel);
        
        if ([rViewModel isKindOfClass:[HippyWormholeViewModel class]]) {
            [self addSubview:rViewModel.view];
        }
    }
}

- (void)addSubview:(UIView *)view
{
    self.frame = view.bounds;
    
    NSString *currentId = nil;
    if (_params.wv_wormholeViewModel)
    {
        currentId = _params.wv_wormholeViewModel.wormholeId;
    }
    
    NSString *preId = nil;
    for (UIView *subview in self.subviews)
    {
        if ([subview isKindOfClass:NSClassFromString(@"HippyWormholeWrapperView")])
        {
            preId = ((HippyWormholeWrapperView *)subview).wormholeId;
            [((HippyWormholeWrapperView *)subview) clearContentView];
        }
        [subview removeFromSuperview];
    }
    
    NSLog(@"ciro debug[Test], currId:%@, oldId:%@", currentId, preId);
    
    [super addSubview:view];
}

- (void)sendEventToWormholeView:(NSDictionary *)eventObj {
    UIView *wormholeView = [[[[self subviews] firstObject] subviews] firstObject];
    NSAssert([wormholeView isKindOfClass:NSClassFromString(@"HippyWormholeItem")], @"view is not wormhole view");
    viewEventSend(wormholeView, eventObj);
}

@end
