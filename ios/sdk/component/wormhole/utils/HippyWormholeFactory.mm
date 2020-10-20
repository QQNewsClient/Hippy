//
//  HippyWormholeFactory.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeFactory.h"
#import "objc/runtime.h"
#import "HippyWormholeItem.h"
#import "HippyWormholeWrapperView.h"
#import "HippyBridge+Private.h"
#import <os/lock.h>

API_AVAILABLE(ios(10.0))
@interface HippyWormholeFactory () {
    __weak HippyBridge *_bridge;
    NSMapTable *_wormholeItemMapTable;
    NSMapTable *_wormholeNodeMapTable;
    NSMapTable *_wormholeModelMapTable;
    os_unfair_lock _unfairLock;
    NSRecursiveLock *_lock;
}

@end

@implementation HippyWormholeFactory

- (instancetype) initWithBridge:(HippyBridge *)bridge {
    self = [super init];
    if (self) {
        _bridge = bridge;
        _wormholeItemMapTable = [NSMapTable strongToWeakObjectsMapTable];
        _wormholeNodeMapTable = [NSMapTable strongToWeakObjectsMapTable];
        _wormholeModelMapTable = [NSMapTable strongToWeakObjectsMapTable];
        if (@available(iOS 10.0, *)) {
            _unfairLock = OS_UNFAIR_LOCK_INIT;
        } else {
            _lock = [NSRecursiveLock new];
        }
    }
    return self;
}

- (void)setWormholeWrapperView:(HippyWormholeWrapperView *)wrapperView forWormholeId:(NSString *)wormholeId {
    if (wormholeId.length == 0)
    {
        return;
    }
    
    [self p_SetObject:wrapperView forKey:wormholeId mapTable:_wormholeItemMapTable];
}

- (void)setWormholeNode:(HippyVirtualWormholeNode *)node forWormholeId:(NSString *)wormholeId {
    if (wormholeId.length == 0)
    {
        return;
    }
    
    [self p_SetObject:(NSObject *)node forKey:wormholeId mapTable:_wormholeNodeMapTable];
}

- (void)setWormholeViewModel:(HippyWormholeViewModel *)model forWormholeId:(NSString *)wormholeId{
    if (wormholeId.length == 0)
    {
       return;
    }
    
    [self p_SetObject:(NSObject *)model forKey:wormholeId mapTable:_wormholeModelMapTable];
}

- (void)removeWormholeWrapperView:(NSString *)wormholeId
{
    if (wormholeId.length == 0)
    {
       return;
    }
    
    [self p_RemoveObjectForKey:wormholeId mapTable:_wormholeItemMapTable];
}

- (void)removeWormholeNode:(NSString *)wormholeId
{
    if (wormholeId.length == 0)
    {
       return;
    }
    
    [self p_RemoveObjectForKey:wormholeId mapTable:_wormholeNodeMapTable];
}

- (void)removeWormholeViewModel:(NSString *)wormholeId
{
    if (wormholeId.length == 0)
    {
       return;
    }
    
    [self p_RemoveObjectForKey:wormholeId mapTable:_wormholeModelMapTable];
}

- (HippyWormholeWrapperView *)wormholeWrapperViewForWormholeId:(NSString *)wormholeId {
    if (wormholeId.length == 0)
    {
        return nil;
    }
    
    return (HippyWormholeWrapperView *)[self p_ObjectForKey:wormholeId mapTable:_wormholeItemMapTable];
}

- (HippyVirtualWormholeNode *)wormholeNodeForWormholeId:(NSString *)wormholeId {
    if (wormholeId.length == 0)
    {
        return nil;
    }
    
    return (HippyVirtualWormholeNode *)[self p_ObjectForKey:wormholeId mapTable:_wormholeNodeMapTable];
}

- (HippyWormholeViewModel *)wormholeViewModelForWormholeId:(NSString *)wormholeId{
    if (wormholeId.length == 0)
    {
        return nil;
    }
    
    return (HippyWormholeViewModel *)[self p_ObjectForKey:wormholeId mapTable:_wormholeModelMapTable];
}

- (void)p_SetObject:(nonnull NSObject *)object forKey:(nonnull NSString *)key mapTable:(NSMapTable *)mapTable
{
    if (@available(iOS 10.0, *)) {
        os_unfair_lock_lock(&_unfairLock);
        [mapTable setObject:object forKey:key];
        os_unfair_lock_unlock(&_unfairLock);
    } else {
        [_lock lock];
        [mapTable setObject:object forKey:key];
        [_lock unlock];
    }
}

- (void)p_RemoveObjectForKey:(nonnull NSString *)key mapTable:(NSMapTable *)mapTable
{
    if (@available(iOS 10.0, *)) {
        os_unfair_lock_lock(&_unfairLock);
        [mapTable removeObjectForKey:key];
        os_unfair_lock_unlock(&_unfairLock);
    } else {
        [_lock lock];
        [mapTable removeObjectForKey:key];
        [_lock unlock];
    }
}

- (NSObject *)p_ObjectForKey:(nonnull NSString *)key mapTable:(NSMapTable *)mapTable
{
    if (@available(iOS 10.0, *)) {
        os_unfair_lock_lock(&_unfairLock);
        id obj = [mapTable objectForKey:key];
        os_unfair_lock_unlock(&_unfairLock);
        
        return obj;
    } else {
        [_lock lock];
        id obj = [mapTable objectForKey:key];
        [_lock unlock];
        
        return obj;
    }
}

- (void)clear
{
    [_wormholeItemMapTable removeAllObjects];
    [_wormholeNodeMapTable removeAllObjects];
    [_wormholeModelMapTable removeAllObjects];
}

@end

@implementation HippyBridge(WormholeFactory)

- (HippyWormholeFactory *)wormholeFactory {
    id batchSelf = self;
//    if ([self isMemberOfClass:[HippyBridge class]]) {
//        batchSelf = [self batchedBridge];
//    }
    HippyWormholeFactory *factory = objc_getAssociatedObject(batchSelf, _cmd);
    if (nil == factory) {
        factory = [[HippyWormholeFactory alloc] initWithBridge:self];
        objc_setAssociatedObject(self, _cmd, factory, OBJC_ASSOCIATION_RETAIN);
    }
    return factory;
}

- (void)setWormholeFactory:(HippyWormholeFactory *)wormholeFactory
{
    objc_setAssociatedObject(self, _cmd, wormholeFactory, OBJC_ASSOCIATION_RETAIN);
}

@end
