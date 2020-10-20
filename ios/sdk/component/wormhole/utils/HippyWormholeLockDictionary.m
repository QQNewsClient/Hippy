//
//  HippyWormholeLockDictionary.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeLockDictionary.h"

@interface HippyWormholeLockDictionary () {
    
    NSRecursiveLock *_lock;
    NSMutableDictionary *_dict;
}

@end

@implementation HippyWormholeLockDictionary

- (instancetype)init {
    
    self = [super init];
    if (self) {
        _lock = [NSRecursiveLock new];
        _dict = [NSMutableDictionary dictionaryWithCapacity:1];
    }
    return self;
}

- (instancetype)initWithMutableDictionary:(NSMutableDictionary *)dic {
    
    self = [self init];
    if (self) {
        if (dic) {
            [_dict setDictionary:dic];
        }
    }
    return self;
}

+ (instancetype)dictionary {
    
    return [self dictionaryWithMutableDictionary:nil];
}

+ (instancetype)dictionaryWithMutableDictionary:(NSMutableDictionary *)dic {
    
    HippyWormholeLockDictionary *lockDic = [[HippyWormholeLockDictionary alloc] initWithMutableDictionary:dic];
    return lockDic;
}

- (id)objectForKey:(id)aKey {
    
    if (aKey == nil) {
        return nil;
    }
    
    [_lock lock];
    id obj = [_dict objectForKey:aKey];
    [_lock unlock];
    
    return obj;
}

- (void)setObject:(id)anObject forKey:(id<NSCopying>)aKey {
    
    if (anObject == nil || aKey == nil) {
        return;
    }
    
    [_lock lock];
    [_dict setObject:anObject forKey:aKey];
    [_lock unlock];
}

- (void)removeObjectForKey:(id)aKey {
    
    if (aKey == nil) {
        return;
    }
    
    [_lock lock];
    [_dict removeObjectForKey:aKey];
    [_lock unlock];
}

- (void)setValue:(id)value forKey:(NSString *)key {
    
    if (key == nil) {
        return;
    }
    
    [_lock lock];
    [_dict setValue:value forKey:key];
    [_lock unlock];
}

- (id)valueForKey:(NSString *)key {
    
    if (key == nil) {
        return nil;
    }
    
    [_lock lock];
    id obj = [_dict valueForKey:key];
    [_lock unlock];
    
    return obj;
}

- (NSArray *)allKeys {
    
    [_lock lock];
    NSArray *keys = [_dict allKeys];
    [_lock unlock];
    
    return keys;
}

- (NSArray *)allValues {
    
    [_lock lock];
    NSArray *values = [_dict allValues];
    [_lock unlock];
    
    return values;
}

- (NSArray *)allKeysForObject:(id)object {
    
    [_lock lock];
    NSMutableArray *arr = [NSMutableArray array];
    NSArray *keys = [_dict allKeysForObject:object];
    [arr addObjectsFromArray:keys];
    [_lock unlock];
    
    return  arr;
}

- (void)setDictionary:(NSMutableDictionary*)dic {
    
    [_lock lock];
    _dict = [[NSMutableDictionary alloc] initWithDictionary:dic];
    [_lock unlock];
}

- (void)addEntriesFromDictionary:(NSDictionary *)otherDictionary {
    
    if (!otherDictionary) {
        return;
    }
    
    [_lock lock];
    [_dict addEntriesFromDictionary:otherDictionary];
    [_lock unlock];
}

- (void)removeAllObjects {
    
    [_lock lock];
    [_dict removeAllObjects];
    [_lock unlock];
}

- (void)removeObjectsForKeys:(NSArray *)keys {
    
    if (keys == nil) {
        return;
    }
    
    [_lock lock];
    [_dict removeObjectsForKeys:keys];
    [_lock unlock];
}

- (NSUInteger)count {
    
    [_lock lock];
    NSUInteger count = [_dict count];
    [_lock unlock];
    
    return count;
}

- (BOOL)writeToFile:(NSString *)path atomically:(BOOL)useAuxiliaryFile {
    
    NSDictionary *dic = [self fetchDictionary];
    
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:dic];
    dic = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    BOOL b = [dic writeToFile:path atomically:useAuxiliaryFile];
    if (!b) {}
    
    return b;
}

- (NSDictionary *)fetchDictionary {
    
    [_lock lock];
    NSDictionary *dict = [NSDictionary dictionaryWithDictionary:_dict];
    [_lock unlock];
    
    return dict;
}

- (void)enumerateKeysAndObjectsUsingBlock:(void (^)(id key, id obj, BOOL *stop))block {
    
    if (!block) return;
    [_lock lock];
    BOOL _stop = NO;
    NSArray *allKeys = [self allKeys];
    for (int i = 0; i < allKeys.count; i++) {
        id key = [allKeys objectAtIndex:i];
        id value = [_dict objectForKey:key];
        block(key, value, &_stop);
        if (_stop) break;
    }
    [_lock unlock];
}

@end
