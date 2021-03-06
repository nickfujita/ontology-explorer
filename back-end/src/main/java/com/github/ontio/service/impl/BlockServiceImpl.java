/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */



package com.github.ontio.service.impl;


import com.github.ontio.dao.BlockMapper;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.IBlockService;
import com.github.ontio.utils.ErrorInfo;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("BlockService")
//@MapperScan("com.github.ontio.dao")
public class BlockServiceImpl implements IBlockService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String VERSION = "1.0";

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private CurrentMapper currentMapper;


    @Override
    public Result queryBlockList(int amount) {

        List<Map> blockList = new ArrayList<>();
        try {
            blockList = queryBlockByPage(0, amount);
        } catch (Exception e) {
            logger.error("db error...",e);
            e.printStackTrace();
            return Helper.result("QueryBlockList", ErrorInfo.DB_ERROR.code(), ErrorInfo.DB_ERROR.desc(), VERSION,false);
        }

        return Helper.result("QueryBlockList", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, blockList);
    }

    @Override
    public Result queryBlockList(int pageSize, int pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0:pageSize * (pageNumber - 1);

        List<Map> blockList = new ArrayList<>();
        Map summaryMap = new HashMap();
        try {
            blockList = queryBlockByPage(start, pageSize);
            summaryMap = currentMapper.selectSummaryInfo();
        } catch (Exception e) {
            logger.error("db error...",e);
            e.printStackTrace();
            return Helper.result("QueryBlockList", ErrorInfo.DB_ERROR.code(), ErrorInfo.DB_ERROR.desc(), VERSION,false);
        }

        int total = (Integer) summaryMap.get("Height");

        Map<String,Object> rs = new HashMap<>();
        rs.put("BlockList", blockList);
        rs.put("Total",total);

        return Helper.result("QueryBlockList", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(),VERSION, rs);
    }


    /**
     * query blocks by page
     *
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    private List<Map> queryBlockByPage (int start, int pageSize) throws Exception{

        List<Map> blockList  = blockMapper.selectBlockByPage(start, pageSize);
        return blockList;
    }

    @Override
    public Result queryBlockByHeight(int height) {

        Map blockInfo = blockMapper.selectBlockByHeight(height);
        if(blockInfo == null) {
            return Helper.result("QueryBlock", ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), VERSION,false);
        }

        List<Map> txnList = transactionDetailMapper.selectTxnByBlockHeight(height);
        blockInfo.put("TxnList", txnList);

        return Helper.result("QueryBlock", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, blockInfo);
    }

    @Override
    public Result queryBlockByHash(String hash) {

        Map blockInfo = blockMapper.selectBlockByHash(hash);

        if(blockInfo == null) {
            return Helper.result("QueryBlock", ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), VERSION, false);
        }

        int blockHeight = (Integer) blockInfo.get("Height");
        List<Map> txnList = transactionDetailMapper.selectTxnByBlockHeight(blockHeight);
        blockInfo.put("TxnList", txnList);

        return Helper.result("QueryBlock", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, blockInfo);
    }

    @Override
    public Result queryBlockGenerateTime(int amount) {

        List<Map> dataList = blockMapper.selectHeightAndTime(amount + 1);
        List<Map> rsList = new ArrayList<>();

        for(int i = 0; i < dataList.size() - 1; i++) {
            int time = (Integer)dataList.get(i).get("BlockTime") - (Integer)dataList.get(i+1).get("BlockTime");
            Map<String, Object> temp = new HashMap<>();
            temp.put("Height", dataList.get(i).get("Height"));
            temp.put("GenerateTime", time);
            rsList.add(temp);
        }

        return Helper.result("QueryBlockGenerateTime", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rsList);
    }
}
