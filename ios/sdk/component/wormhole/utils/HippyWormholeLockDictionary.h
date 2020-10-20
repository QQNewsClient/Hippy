//
//  HippyWormholeLockDictionary.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HippyWormholeLockDictionary : NSObject

- (instancetype)initWithMutableDictionary:(NSMutableDictionary *)dic;
+ (instancetype)dictionary;
+ (instancetype)dictionaryWithMutableDictionary:(NSMutableDictionary *)dic;

- (id)objectForKey:(id)aKey;
- (void)setObject:(id)anObject forKey:(id<NSCopying>)aKey;
- (void)removeObjectForKey:(id)aKey;

- (void)setValue:(id)value forKey:(NSString *)key;
- (id)valueForKey:(NSString *)key;

- (NSArray *)allKeys;
- (NSArray *)allValues;
- (NSArray *)allKeysForObject:(id)object;

- (void)setDictionary:(NSMutableDictionary*)dic;
- (void)addEntriesFromDictionary:(NSDictionary *)otherDictionary;

- (void)removeAllObjects;
- (void)removeObjectsForKeys:(NSArray *)keys;

- (NSUInteger)count;

- (BOOL)writeToFile:(NSString *)path atomically:(BOOL)useAuxiliaryFile;

- (NSDictionary *)fetchDictionary;
- (void)enumerateKeysAndObjectsUsingBlock:(void (^)(id key, id obj, BOOL *stop))block;

@end
