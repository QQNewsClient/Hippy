/*!
 * iOS SDK
 *
 * Tencent is pleased to support the open source community by making
 * Hippy available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import <Foundation/Foundation.h>
#import "HippyUIManager.h"

@class HippyVirtualNode;
@class HippyVirtualList;
@class HippyVirtualCell;

@protocol HippyVirtualListComponentUpdateDelegate
- (void)virtualListDidUpdated;
@end

typedef NS_ENUM(NSUInteger, HippyBaseListViewItemDataType) {
    HippyBaseListViewItemDataTypeDefault, //默认值，数据来源同JS Bundle
    HippyBaseListViewItemDataTypeWormhole,    //数据来源于虫洞JS
    HippyBaseListViewItemDataTypeNative,  //数据来源于native绘制
};

@interface HippyVirtualNode : NSObject <HippyComponent>

+ (HippyVirtualNode *)createNode:(NSNumber *)hippyTag viewName:(NSString *)viewName props:(NSDictionary *)props;

- (instancetype)initWithTag:(NSNumber *)hippyTag viewName:(NSString *)viewName props:(NSDictionary *)props;

@property (nonatomic, retain) NSMutableArray<HippyVirtualNode *> *subNodes;

@property (nonatomic, weak) HippyVirtualList *listNode;
@property (nonatomic, weak) HippyVirtualCell *cellNode;
@property (nonatomic, copy) NSNumber *rootTag;

@property (nonatomic, assign) HippyBaseListViewItemDataType dataType;

@property (nonatomic, weak) id owner;

- (BOOL)isListSubNode;

//判断当前node是否使用懒加载，和下面方法在于，这个方法仅通过self class判断是否是懒加载类型，默认NO
- (BOOL)isLazilyLoadType;

//判断当前node是否使用懒加载，和上面方法不同在于，如果parent node需要懒加载，当前node同样也懒加载。
- (BOOL)createViewLazily;

//通过isLazilyLoadType查找第一个懒加载的parent node
- (HippyVirtualNode *)firstLazilyLoadTypeParentNode;

typedef UIView * (^HippyCreateViewForShadow)(HippyVirtualNode *node);
typedef UIView * (^HippyUpdateViewForShadow)(HippyVirtualNode *newNode, HippyVirtualNode *oldNode);
typedef void (^HippyInsertViewForShadow)(UIView *container, NSArray<UIView *> *childrens);
typedef void (^HippyRemoveViewForShadow)(NSNumber *hippyTag);
typedef void (^HippyVirtualNodeManagerUIBlock)(HippyUIManager *uiManager, NSDictionary<NSNumber *, HippyVirtualNode *> *virtualNodeRegistry);

- (UIView *)createView:(HippyCreateViewForShadow)createBlock insertChildrens:(HippyInsertViewForShadow)insertChildrens;
- (NSDictionary *)diff:(HippyVirtualNode *)newNode;

- (void)removeView:(HippyRemoveViewForShadow)removeBlock;

@end

@interface HippyVirtualCell : HippyVirtualNode
@property (nonatomic, copy) NSString *itemViewType;
@property (nonatomic, assign) BOOL sticky;
@property (nonatomic, weak) UIView *cell;
@end

@interface HippyVirtualList : HippyVirtualNode
@property (nonatomic, assign) BOOL needFlush;
@end

@interface UIView (HippyRemoveNode)
- (void)removeView:(HippyRemoveViewForShadow)removeBlock;
@end
