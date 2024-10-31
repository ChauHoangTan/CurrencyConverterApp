# Currency Converter App



## App Structure
The app is structured in the following way:
- **Adapter**: Contains `SpinnerItemAdapter` to manage custom spinner items.
- **APIServices**: Includes `CurrencyFlagsAPI` and `ExchangeRatesAPI` classes to fetch currency flags and exchange rates.
- **Model**: Contains data models, including `ExchangeRatesResponse` and `FlagCurrencyResponse`.
- **Utilities**: 
  - `CallApiClient.kt` manages API calls.
  - `CurrencyConverter.kt` handles currency conversion calculations.
- **MainActivity**: Main entry point of the application, managing the UI and interactions.

## How to Build and Run the App
1. **Clone the Repository**: 
   ```bash
   git clone <repository-url>
2. **Open the Project: Open the cloned project in Android Studio.**
3. **Build the Project: Click on Build -> Make Project or use the shortcut Ctrl+F9.**
4. **Run the App: Click the Run button or press Shift+F10 to run on an emulator or a connected device.**

## Additional notes and challenges encountered
1. I used ExchangeRatesAPI to get the rate of each currency follow EUR
2. Then I used RestCountriesAPI to get the flag of a currency follow country
3. But I encountered a problem about get flags, because some curriences don't have specific flag follow country ( ex: EUR )
4. So I could get these flags, I decided to show empty images of that cases.

## Link Demo
** **
