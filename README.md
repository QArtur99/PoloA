# PoloA
PolaA is a automated trading system for Poloniex cryptocurrency exchange platform. It is multithreaded application built in MVP architecture by Dagger2, Retrofit2, RxJava2 in order to provide highest possible efficiency. Trading strategy of this system base on technical indicators such as EMA(Exponential Moving Average) and RMI(Relative Momentum Index)


### How to run the project in development mode
* Clone or download repository as a zip file.
* Create `poloa.properties` file.
* Set two variables in `poloa.properties` KEY and SECRET.
* Open project in Android Studio.
* Set path to `poloa.properties` in `build.gradle`(line 4).
* Set PoloA settings in `poloa/presenter/utility/Settings`.
* Run 'app' `SHIFT+F10`.
* To stop this trading system run gradle build again.


### Settings
* ##### Global
  * sell_if_dropped_percentage
  * sell_if_raised_percentage
  * can_i_lose
* ##### RMI
  * time_period
  * rmi_over_signal
  * signal_over_rmi
  * length
  * momentum
  * signal
  * overbought
  * oversold
* ##### EMA
  * time_period
  * length
  * percentage_value


### Trading Strategy Scheme
![schemat strategii 30 07](https://user-images.githubusercontent.com/25232443/43411361-370687ce-942a-11e8-9239-79a203ed1d50.jpg)



### Application structure diagram
![schemat programu 30 07](https://user-images.githubusercontent.com/25232443/43414252-8b25d0b4-9432-11e8-9a1f-3e4489104570.jpg)



### Screenshots
![stykowka-001](https://user-images.githubusercontent.com/25232443/43417105-58fcb05a-943a-11e8-8002-516e8ca7b653.jpg)



### Trading Risk Disclaimer
Remember that the information presented here is for educational purpose only! It is not meant to be taken as direct investment advice. I'm not responsible for your investment actions. All trading operations involve high risks of losing your entire investment. You must therefore decide your own suitability to trade. Trading results can never be guaranteed. This is not an offer to buy or sell cryptocurrencies, stock, forex, futures, options, commodity interests or any other trading security.