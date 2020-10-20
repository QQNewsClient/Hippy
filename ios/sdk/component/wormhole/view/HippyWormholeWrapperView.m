//
//  HippyWormholeWrapperView.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeWrapperView.h"
#import "HippyVirtualWormholeNode.h"
#import "HippyWormholeEngine.h"
#import "HippyWormholeItem.h"
#import "HippyWormholeBusinessHandler.h"

@interface HippyWormholeWrapperView ()

@end

@implementation HippyWormholeWrapperView{
    UIView * _contentView;
    UIView * _natvieView;
}
@synthesize wormholeId = _wormholeId;

- (instancetype)initWithWormholeId:(NSString *)wormholeId natvieView:(UIView * _Nullable)nativeView{
    if (self = [super init]) {
        self.clipsToBounds = true;
        self.wormholeId = wormholeId;
        _natvieView = nativeView;
        _natvieView.userInteractionEnabled = NO;
        if (_natvieView) {
            if ([HippyWormholeEngine sharedInstance].nativeVueDebugEnabled)
            {
                _natvieView.backgroundColor = [UIColor colorWithWhite:0 alpha:0.3];
            }
            
            [self addSubview:_natvieView];
            self.frame = _natvieView.bounds;
        }
    }
    return self;
}

- (void)setContentView:(UIView *)contentView{
    if (_contentView != contentView || contentView.superview != self) {
        if (_contentView.superview == self) {
            [_contentView removeFromSuperview];
        }
        
        if (contentView.superview != self)
        {
            [contentView removeFromSuperview];
        }
        
        [_contentView removeObserver:self forKeyPath:@"frame"];
        
        _contentView = contentView;
        
        [contentView addObserver:self forKeyPath:@"frame" options:NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld context:NULL];
        [self insertSubview:contentView atIndex:0];//insert contentview below nvview
        self.frame = contentView.bounds;
        [self dispatchViewSizeDidChangedEvent];
        [self _removeNativeView];
    }
}

- (UIView *)contentView {
    return _contentView;
}

- (void)clearContentView
{
    if (_contentView)
    {
        [_contentView removeFromSuperview];
        [_contentView removeObserver:self forKeyPath:@"frame"];
        if ([_contentView isKindOfClass:NSClassFromString(@"HippyWormholeItem")])
        {
            [_contentView.bridge.uiManager removeNativeNodeView:_contentView];
            NSString *wormholeId = ((HippyWormholeItem *)_contentView).wormholeId;
            NSLog(@"ciro debug:[WormholeEvent DidDisappear]. wormholeId:%@", wormholeId);
            NSInteger index = ((HippyWormholeItem *)_contentView).businessIndex;
            if (wormholeId.length > 0)
            {
                [[HippyWormholeEngine sharedInstance].businessHandler notifyWormholeEvent:HippyWormholeEventViewDidDisappear extraData:@{@"wormholeId": wormholeId, @"index": @(index)}];
            }
        }
        _contentView = nil;
    }
}

- (void)updateContentViewWithUserInfo:(NSDictionary *)userInfo {
    [self.delegate wrapView:self updateProps:userInfo];
}

- (void)setDelegate:(id<HippyWormholeWrapperViewDelegate>)delegate{
    if (_delegate != delegate) {
        _delegate = delegate;
        if (_contentView) {//fill delegate
            [self dispatchViewSizeDidChangedEvent];
        }
    }
}

- (NSString *)description
{
    return [NSString stringWithFormat:@"%@; wormhole:%@", [super description], _wormholeId];
}

- (void)dealloc
{
    if (_contentView)
    {
        [_contentView removeObserver:self forKeyPath:@"frame"];
    }
}

#pragma mark - observe
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context
{
    CGRect frame1 = [change[@"new"] CGRectValue];
    CGRect frame2 = [change[@"old"] CGRectValue];
    if (!CGRectEqualToRect(frame1, frame2)) {
        CGRect selfFrame = self.frame;
        selfFrame.size = frame1.size;
        [self setFrame:selfFrame];
        [self dispatchViewSizeDidChangedEvent];
    }
}

#pragma mark -- private

- (void)_removeNativeView{
    UIView * nativeView = _natvieView;
    _natvieView = nil;
    CGFloat delayTime = 0.15;

    if ([HippyWormholeEngine sharedInstance].nativeVueDebugEnabled)
    {
        delayTime = 2;
    }
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delayTime * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [UIView animateWithDuration:0.15 delay:0 options:(UIViewAnimationOptionCurveLinear) animations:^{
                 nativeView.alpha = 0;
             } completion:^(BOOL finished) {
                 [nativeView removeFromSuperview];
                 if ([self.delegate respondsToSelector:@selector(wrapView:didRemoveNativeView:)]) {
                     [self.delegate wrapView:self didRemoveNativeView:nativeView];
                 }
                 NSLog(@"%s %d",__FUNCTION__,finished);
             }];
    });
   
}

- (void)dispatchViewSizeDidChangedEvent{
    if ([self.delegate respondsToSelector:@selector(wrapView:didChangedSize:)]) {
        if ([[NSThread currentThread] isMainThread])
        {
            [self.delegate wrapView:self didChangedSize:self.frame.size];
        }
        else
        {
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.delegate wrapView:self didChangedSize:self.frame.size];
            });
        }
    }
}

@end
