//
//  HippyNVUtil.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HippyNVUtil : NSObject

+ (NSNumber * _Nullable)toNSNumber:(NSObject * _Nullable)object;
+ (NSString * _Nullable)toNSString:(NSString * _Nullable)object;
+ (NSDictionary * _Nonnull)toNSDictionary:(NSDictionary * _Nullable)object;
+ (NSMutableDictionary * _Nonnull)toNSMutableDictionary:(NSDictionary * _Nullable)object;

@end

NS_ASSUME_NONNULL_END
