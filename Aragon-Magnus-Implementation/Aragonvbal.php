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


//This is developed by Magnus Collective. This program is called 
//when we need the "Ack VOTE" token balance.




<?php

//Giving some space before publishing the results.
echo "<br>";
echo "<br>";
echo "<br>";
//Information about the Ack VOTE tokens created.

echo "Acknowledgment Stocks(Ack stock(VOTE)) are created by the Organization in ARAGON and issued to Robots connected to the Eco system. Each Robot based on its hierar$
echo "<br>";
echo "The results below shows the number of tokens each Robot hold.";
echo "<br>";
echo "<br>";

//calling the nodejs scripts.

$output1 = shell_exec('node <path to the .js file>/Aragonbal1.js');
echo "$output1";
echo "<br>";
$output2 = shell_exec('node <path to the .js file>/Aragonbal2.js');
echo "$output2";
echo "<br>";
$output3 = shell_exec('node <path to the .js file>/Aragonbal3.js');
echo "$output3";
echo "<br>";
$output4 = shell_exec('node <path to the .js file>/Aragonbal4.js');
echo "$output4";
echo "<br>";
echo "<br>";
echo "<br>";

echo "note:- This is an example case";
echo "<br>";
echo "<br>";
echo "These Tokens are used by Robots to vote in a poll created in-case of Data discrepancy";
echo "<br>";


?>
