/*This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

//This is developed by Magnus Collective. This program is called //when we need the "Ack VOTE" token balance.


const Web3 = require("web3");
const web3 = new Web3();
web3.setProvider(new
web3.providers.HttpProvider("https://kovan.infura.io/<yourkey>"));

var addr1 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX');
var addr2 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX');
var addr3 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX');
var addr4 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX');

// Token contract address, used call the token balance of the address
var contractAddr = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX');
// Get the address ready for the call, substring removes the '0x', as its not required
var tknAddress1 = (addr1).substring(2);
var tknAddress2 = (addr2).substring(2);
var tknAddress3 = (addr3).substring(2);
var tknAddress4 = (addr4).substring(2);

// '0x70a08231' is the contract 'balanceOf()' ERC20 token function in hex. A zero buffer is required and then we add the previously defined address with tokens
var contractData1 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' + tknAddress1);
var contractData2 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' + tknAddress2);
var contractData3 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' + tknAddress3);
var contractData4 = ('0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' + tknAddress4);

//function to give delay in exection
function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

// Now we call the token contract with the variables from above, response will be a big number string
web3.eth.call({
    to: contractAddr, // Contract address, used call the token balance of the address in question
    data: contractData3 // Combination of contractData and tknAddress, required to call the balance of an address
    }, function(err, result) {
        //if result is '0x' then its not the correct value.
        if (result != '0x') {
                var tokens = web3.utils.toBN(result).toString(); // Convert the result to a usable number string
                console.log('Ack Tokens(VOTE) Owned by Silent Rain: ' + tokens);
        }
        else {
                console.log('Ack Tokens(VOTE) Owned by Silent Rain: error in retriving Token details please refresh the page.'); // Dump errors here
        }

});
