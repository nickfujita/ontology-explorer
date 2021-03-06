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



package com.github.ontio.controller;

import com.github.ontio.paramBean.Result;
import com.github.ontio.service.impl.TransactionServiceImpl;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/15
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/api/v1/explorer/")
public class TransactionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private TransactionServiceImpl transactionService;


    /**
     * query the last few transactions
     *
     * @return
     */
    @RequestMapping(value = "/transactionlist/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryTransactionList(@PathVariable("amount") int amount) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("amount:{}", amount);

        Result rs = transactionService.queryTxnList(amount);
        return rs;
    }


    /**
     * query transactions by page
     *
     * @return
     */
    @RequestMapping(value = "/transactionlist/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryTxn(@PathVariable("pagesize") Integer pageSize,
                           @PathVariable("pagenumber") Integer pageNumber) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("pageSize:{}, pageNumber:{}",pageSize, pageNumber);

        Result rs = transactionService.queryTxnList(pageSize,pageNumber);
        return rs;
    }

    /**
     * query transaction by txnhash
     *
     * @return
     */
    @RequestMapping(value = "/transaction/{txnhash}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryTxnByHash(@PathVariable("txnhash") String txnHash) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("txnHash:{}",txnHash);

        Result rs = transactionService.queryTxnDetailByHash(txnHash);
        return rs;
    }



    /**
     * query asset balance and transactions by address
     *
     * @return
     */
    @RequestMapping(value = "/address/{address}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressInfo(@PathVariable("address") String address,
                                   @PathVariable("pagesize") int pageSize,
                                   @PathVariable("pagenumber") int pageNumber) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},pagesize:{},pagenumber:{}",address, pageSize, pageNumber);

        Result rs = transactionService.queryAddressInfo(address, pageNumber, pageSize);
        return rs;
    }


    /**
     * query the specially asset balance and transactions
     *
     * @return
     */
    @RequestMapping(value = "/address/{address}/{assetname}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressInfo(@PathVariable("address") String address,
                                   @PathVariable("pagesize") int pageSize,
                                   @PathVariable("pagenumber") int pageNumber,
                                   @PathVariable("assetname") String assetName) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},pagesize:{},pagenumber:{}, assetname:{}",address, pageSize, pageNumber, assetName);

        Result rs = transactionService.queryAddressInfo(address, pageNumber, pageSize, assetName);
        return rs;
    }


    /**
     * query the specially asset balance and transactions
     *
     * @return
     */
    @RequestMapping(value = "/address/bytime/{address}/{assetname}/{pagesize}/{time}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressInfoByTime(@PathVariable("address") String address,
                                         @PathVariable("pagesize") int pageSize,
                                         @PathVariable("assetname") String assetName,
                                         @PathVariable("time") int time) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},assetname:{},pageSize:{},time:{}", address, assetName, pageSize, time);

        Result rs = transactionService.queryAddressInfoByTime(address, assetName, pageSize, time);
        return rs;
    }

}
