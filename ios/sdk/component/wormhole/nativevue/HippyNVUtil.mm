//
//  HippyNVUtil.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyNVUtil.h"

@implementation HippyNVUtil

+ (NSNumber * _Nullable)toNSNumber:(NSObject * _Nullable)object {
    if ([object isKindOfClass:[NSString class]]) {
        return @([((NSString *)object) longLongValue]);
    }
    return [object isKindOfClass:[NSNumber class]] ? (NSNumber *)object : nil;
}

+ (NSString * _Nullable)toNSString:(NSString * _Nullable)object{
   if ([object isKindOfClass:[NSNumber class]]) {
       return [((NSNumber *)object) stringValue];
   }
   return [object isKindOfClass:[NSString class]] ? (NSString *)object : nil;
}

+ (NSDictionary * _Nonnull)toNSDictionary:(NSDictionary * _Nullable)object{
    if ([object isKindOfClass:[NSDictionary class]]) {
        return object;
    }
    return @{};
}

+ (NSMutableDictionary * _Nonnull)toNSMutableDictionary:(NSDictionary * _Nullable)object {
    if ([object isKindOfClass:[NSMutableDictionary class]]) {
        return (NSMutableDictionary *)object;
    }
    if ([object isKindOfClass:[NSDictionary class]]) {
        return (NSMutableDictionary *)[object mutableCopy];
    }
    return [@{} mutableCopy];
}
@end
