<template>
  <div class="container container-margin-top">
    <div class="row">
      <div class="col">
        <p class="title-more float-left font-Regular normal_color font-size18 block-detail-page-check-hand" @click="toReturn"><< {{ $t('all.return') }}</p>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <p  class="text-center font-size40 font-ExtraLight p_margin_bottom_L normal_color">ADDRESSES - Position Ranking</p>
      </div>
    </div>

    <div class="row justify-content-center table-margin-bottom">
      <div class="col-12">
        <table class="table table-hover">
          <thead>
          <tr>
            <th class="blp-ab-border-top-none font-size18" scope="col" style="padding-top:34px;" >{{ $t('addressList.rank') }}</th>
            <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.name') }}</th>
            <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.balance') }}</th>
            <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.percent') }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(address,index) in addressList.info">
            <td class="font-size14 font-Regular normal_color td_height3">{{Number(addressList.basicRank) + index}}</td>
            <td class="font-size14 font-Regular important_color td_height3 click_able" @click="goToAddressDetail(address.address)">{{address.address}}</td>
            <td class="font-size14 font-Regular normal_color td_height3">{{address.balance}}</td>
            <td class="font-size14 font-Regular normal_color td_height3">
              <div class="progress" style="position: relative">
                <div class="progress-bar bg-info" :style="'width:'+ (address.percent * 100).toFixed(6) + '%'">
                  <span class="black-color" style="position: absolute; right: 5px;">{{(address.percent * 100).toFixed(4)}} %</span>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="row justify-content-center">
      <div id="page">
        <ul class="pagination">
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.nextPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import Helper from './../../helpers/helper.js'

  export default {
    name: "address-list-page",
    created() {
      this.getAddressListInfo()
    },
    watch: {
      '$route': 'getAddressListInfo'
    },
    computed: {
      ...mapState({
        addressList: state => state.AddressListPage.AddressListDetail,
      }),
    },
    methods: {
      getAddressListInfo() {
        this.$store.dispatch('getAddressListPage', this.$route.params).then()
      },
      goToAddressDetail(address) {
        if (this.$route.params.net == undefined) {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: address, pageSize: 10, pageNumber: 1}
          })
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: address, pageSize: 10, pageNumber: 1, net: 'testnet'}
          })
        }
      },
      toReturn() {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'Home'})
        } else {
          this.$router.push({name: 'HomeTest', params: {net: 'testnet'}})
        }
      },
      goToPage($Page) {
        if($Page.pageNumber === 0) {
          return false
        } else {
          if (this.$route.params.net == undefined) {
            this.$router.push({name: 'addressList', params: {pageSize: $Page.pageSize, pageNumber: $Page.pageNumber}})
          } else {
            this.$router.push({
              name: 'addressListTest',
              params: {pageSize: $Page.pageSize, pageNumber: $Page.pageNumber, net: 'testnet'}
            })
          }
        }
      },
      getTime($time) {
        return Helper.getDateTime($time)
      },
      getDate($time) {
        return Helper.getDate($time)
      },
    }
  }
</script>

<style scoped>
  .blp-ab-border-top-none{
    border-top: none;
  }

  .table-margin-bottom {
    margin-bottom: 16px;
  }
</style>
