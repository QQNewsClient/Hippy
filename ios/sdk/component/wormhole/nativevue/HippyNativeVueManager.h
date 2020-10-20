//
//  HippyNativeVueManager.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@protocol NativeVueProtocol;

@interface HippyNativeVueManager : NSObject

@property(nonatomic, strong, nullable) id<NativeVueProtocol> handler;//should be retain

+ (instancetype)shareInstance;

- (BOOL)loadResource:(NSData *)resource;

- (NSString *)jsonDomWithTemplateId:(NSString *)templatedId;

- (void)registerGlobalVaribleWithKey:(NSString *)key value:(NSObject *)value;

- (NSDictionary *)virtualDomWithJsonDom:(NSString *)jsonDom domData:(NSDictionary *)domData;


@end

@protocol NativeVueProtocol <NSObject>

- (void)registerGlobalVaribleWithKey:(NSString *)key value:(NSObject *)value;
- (NSDictionary *)virtualDomWithJsonDom:(NSString *)jsonDom domData:(NSDictionary *)domData;

@end

NS_ASSUME_NONNULL_END
