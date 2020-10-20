//
//  HippyWormholeItem.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeItem.h"
#import "HippyBridge.h"
#import "HippyTouchHandler.h"
#import "HippyWormholeFactory.h"

@interface HippyWormholeItem () {
    HippyBridge *_bridge;
    HippyTouchHandler *_touchHandler;
}

@end

@implementation HippyWormholeItem
@synthesize wormholeId = _wormholeId;

- (instancetype)initWithBridge:(HippyBridge *)bridge {
    self = [super init];
    if (self) {
        _bridge = bridge;
        _touchHandler = [[HippyTouchHandler alloc] initWithRootView:self bridge:bridge];
        [self addGestureRecognizer:_touchHandler];
        _businessIndex = -1000;  // default value
    }
    return self;
}

- (void)addSubview:(UIView *)view{
    [super addSubview:view];
}

- (NSString *)description
{
    return [NSString stringWithFormat:@"%@; wormhole:%@", [super description], _wormholeId];
}

- (void)setParams:(NSDictionary *)params {
    if (_params != params)
    {
        _params = params;
        if (_params[@"wormholeId"])
        {
            _wormholeId = _params[@"wormholeId"];
        }
        
        if (_params[@"index"])
        {
            _businessIndex = [_params[@"index"] integerValue];
        }
    }
}

@end
