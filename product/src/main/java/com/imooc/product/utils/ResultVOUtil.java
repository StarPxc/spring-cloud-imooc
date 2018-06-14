package com.imooc.product.utils;

import com.imooc.product.vo.ResultVO;

import java.util.List;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.utils
 * @date 2018/6/14  19:53
 * @Description: TODO
 */
public class ResultVOUtil {
      public static ResultVO success(Object object){
          ResultVO resultVO=new ResultVO();
          resultVO.setData(object);
          resultVO.setCode(0);
          resultVO.setMsg("成功");
          return resultVO;
      }
}
