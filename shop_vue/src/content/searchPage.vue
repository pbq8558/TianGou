<template>
    <div>
        <nav_top :flag_scroll="true" :flag_fixed="false"/>
        <nav_right/>

        <div class="topFixed_serarch" ref="topFixed_serarch">
          <img src="../assets/tmall.jpg">
          <div style="position: relative;top: -40px;left:23%">
            <serarch @event_click_search_hotWord="event_click_search_hotWord" :flag_home="false"/>
          </div>
        </div>



        <div class="container">
          <div class="row second">
              <img src="../assets/tmall.jpg">
              <div class="serarch">
                <serarch  @event_click_search_hotWord="event_click_search_hotWord" :flag_bottom_work="true" :flag_home="false"/>
              </div>
          </div>

          <div class="row"><div class="split"></div></div>

          <div v-show="flag_containData">
            <div class="row">
              <div class="col-lg-1-5 spc" v-for="itemData in commodityList">
                <SearchPage_commodity  :props_CommodityMessage="itemData"/>
              </div>
            </div>
            <div class="pageBox">
              <el-pagination background="true"
               layout="total, prev, pager, next"
               :total="commoditySum"
               :page-size="pageStepSize"
               :current-page.sync="pageNo">
              </el-pagination>
            </div>


          </div>

        <div class="notData" v-show="!flag_containData">
          <img src="../assets/search_notdata.png">
          <span class="side">旺~没找到与</span>
          <span class="maddle">“ {{search_word}} ”</span>
          <span class="side">相关的商品哦。</span>
        </div>

      </div>

    </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    import serarch from "@/components/serarch";
    import SearchPage_commodity from "@/components/searchPage_commodity";
    import Nav_right from "@/components/nav_right";
    export default {
        name: "searchPage",
      components: {Nav_right, SearchPage_commodity, serarch, Nav_top},
      data(){
          return{
            flag_topFixed_serarch:false,
            flag_containData:true,
            commoditySum:0, //搜索到的商品总数
            commodityList:[],  //搜索到的商品列表 分页

            pageNo:1,//  分页 页码
            pageStepSize:10 ,//  分页 步长

            flag_backTop:false
          }
      },
      computed:{
        search_word(){
          return this.$store.state.search_word;
        }
      },
      watch:{
        pageNo(val){
            this.sendSearch();
            this.backTop();
        },
        flag_topFixed_serarch(val){
          if (val){
            this.$refs.topFixed_serarch.style.transform = "translate(0px,-37px)"
          } else {
            this.$refs.topFixed_serarch.style.transform = "translate(0px,-90px)"
          }
        }

      },
      methods:{// 点击图片回到顶部方法，加计时器是为了过渡顺滑
        backTop () {
          let that = this;
          let timer = setInterval(() => {
            let ispeed = Math.floor(-that.scrollTop / 5)
            document.documentElement.scrollTop = document.body.scrollTop = that.scrollTop + ispeed;
            if (that.scrollTop == 0) {
              clearInterval(timer)
            }
          }, 0.6)
        },
        // 为了计算距离顶部的高度，当高度大于xx显示回顶部图标，小于x则隐藏
        scrollToTop () {
          let that = this;
          let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
          that.scrollTop = scrollTop;
          if (that.scrollTop > 100) {
            that.flag_topFixed_serarch = true
          } else {
            that.flag_topFixed_serarch = false
          }
        },



          sendSearch(){
            this.$axios.get('/commodity/selSearchVO',{
              params:{
                searchWord: this.$store.state.search_word,
                pageNo : this.pageNo,
                pageStepSize : this.pageStepSize
              }
            }).then(res=>{
              this.commoditySum = res.data.commoditySum;
              this.commodityList = res.data.searchCommodityVOList;
              this.flag_containData=this.commoditySum>0;
            });
            // 第一次 不
            if (this.flag_backTop){
              this.backTop();
            }else this.flag_backTop=true;
          },
          event_click_search_hotWord(){
              this.pageNo = 1;
              this.sendSearch();
          }
      },
      created() {
        this.sendSearch();
      },
      mounted () {
        window.addEventListener('scroll', this.scrollToTop)
      },
      destroyed () {
        window.removeEventListener('scroll', this.scrollToTop)
      },
    }
</script>

<style scoped>
  
  .go-top{
    cursor: pointer;
    height: 90px;
    position: fixed;
    bottom: 20px;
    right: 20px;
  }

  .topFixed_serarch{
    margin-left: 5%;
    width: 90%;
    height: 50px;
    background: white;
    position: fixed;
    z-index: 50;
    border: 1px solid #e8e8e8;

    transform: translate(0px,-90px);
    transition:transform 0.8s;
    -moz-transition:transform 0.8s; /* Firefox 4 */
    -webkit-transition:transform 0.8s; /* Safari and Chrome */
    -o-transition:transform 0.8s; /* Opera */
  }
  
  
  .spc{
    margin-top: 20px;
  }
  
  .container{
    max-width: 1380px;
  }




  .second{
    padding-top: 15px;
    padding-left: 10px;
    padding-bottom: 1px;
    height: 100px;
    width: 100%;
  }
  .second img{
    width: 220px;
    height: 40px;
  }
  .serarch{
    margin-left: 100px;
    margin-top: 8px;
  }
  .split{
    margin: 0 auto;
    height: 1px;
    width: 98%;
    background: #E5E5E5;

  }
  .notData{
    background: #FFF8F6;
    border: 1px solid #F7EAE7;
    height: 90px;
  }
  .notData img{
    height: 60px;
    margin: 15px;
  }
  .side{
    color: #595959;
  }
  .maddle{
    color: #790103;
    font-weight: bold;
  }
  .pageBox{
    float: right;
    margin-top: 40px;
    margin-bottom: 70px;
  }




  @media (min-width: 768px) {
    .col-sm-1-5 {
      width: 20%;
      float: left;
    }
  }

  @media (min-width: 1200px) {
    .col-md-1-5 {
      width: 20%;
      float: left;
    }
  }

  @media (min-width: 1300px) {
    .col-lg-1-5 {
      width: 20%;
      float: left;
    }
  }

</style>
