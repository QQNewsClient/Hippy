//
//  HippyWormholeTagShadowView.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeTagShadowView.h"
#import "HippyWormholeViewModel.h"
#import "HippyWormholeTagView.h"
#import "NSObject+WhormholdViewModel.h"

@interface HippyWormholeTagShadowView ()<HippyWormholeViewModelDelegate>

@end

@implementation HippyWormholeTagShadowView{
    NSDictionary * _params;
}

@synthesize wormholeId = _wormholeId;
@synthesize viewModel = _viewModel;

- (void)setProps:(NSDictionary *)props{
    [super setProps:props];
    if (self.props[@"params"] && [self.props[@"params"] isKindOfClass:[NSDictionary class]]){
        _params = (NSDictionary *)self.props[@"params"];
        _params.wv_wormholeViewModel = self.viewModel;//顺应hippy设计
        [self.viewModel attachViewModelPropsToShadowView:self withOriginProps:(NSMutableDictionary *)props];//附加viewModel相关属性
    }else {
        HippyAssert(false,@"should contain params");
    }
}

#pragma mark - getter

- (HippyWormholeViewModel *)viewModel{
    if (!_viewModel) {
        _viewModel = [[HippyWormholeViewModel alloc] initWithParams:self.props[@"params"]];
        _viewModel.delegate = self;
        _wormholeId = _viewModel.wormholeId;
        [_viewModel syncBuild];//同步构建
    }
    return _viewModel;
}

#pragma mark - HippyWormholeViewModelDelegate

- (void)wormholeViewModel:(HippyWormholeViewModel *)viewModel didChangedSize:(CGSize)size
{
    CGFloat height = self.height ? self.height : _viewModel.viewHeight;
    if (fabs(height - size.height) < 1 ) {//最小高度差异更新条件
        return ;
    }
    [_viewModel updateShadowView:self onWormholeSizeChanged:size];
}

@end
