
pragma solidity >=0.4.18;

import 'localhost/contracts/token/StandardToken.sol';
import 'localhost/contracts/ownership/Ownable.sol';
import 'localhost/contracts/ownership/Contactable.sol';


contract MagnusCoin is StandardToken, Ownable, Contactable {
	//put the desired coin name, symbol and expiry time.
    string public name = "xxxx";
    string public symbol = "xxxx";
    uint256 public constant decimals = 18;

    mapping (address => bool) internal allowedOverrideAddresses;

    bool public tokenActive = false;
    
    uint256 endtime = xxxxxxxxxx;

    modifier onlyIfTokenActiveOrOverride() {
        require(tokenActive || msg.sender == owner || allowedOverrideAddresses[msg.sender]);
        _;
    }

    modifier onlyIfTokenInactive() {
        require(!tokenActive);
        _;
    }

    modifier onlyIfValidAddress(address _to) {
        require(_to != 0x0);
        require(_to != address(this));
        _;
    }

    event TokenActivated();
    event TokenDeactivated();
    

    function MagnusCoin() public {
		
		//put actual values here.
        totalSupply = xxxxxxxxxxxxxxxxxxxxxxxxxx;
        contactInformation = "xxxxxxxxxxxxxxx";
        
        balances[msg.sender] = totalSupply;
    }

    function approve(address _spender, uint256 _value) public onlyIfTokenActiveOrOverride onlyIfValidAddress(_spender) returns (bool) {
        return super.approve(_spender, _value);
    }

    function transfer(address _to, uint256 _value) public onlyIfTokenActiveOrOverride onlyIfValidAddress(_to) returns (bool) {
        return super.transfer(_to, _value);
    }

    function ownerSetOverride(address _address, bool enable) external onlyOwner {
        allowedOverrideAddresses[_address] = enable;
    }
    

    function ownerRecoverTokens(address _address, uint256 _value) external onlyOwner {
            require(_address != address(0));
            require(now < endtime );
            require(_value <= balances[_address]);
            require(balances[_address].sub(_value) >=0);
            balances[_address] = balances[_address].sub(_value);
            balances[owner] = balances[owner].add(_value);
            Transfer(_address, owner, _value);
    }

    function ownerSetVisible(string _name, string _symbol) external onlyOwner onlyIfTokenInactive {        

        name = _name;
        symbol = _symbol;
    }

    function ownerActivateToken() external onlyOwner onlyIfTokenInactive {
        require(bytes(symbol).length > 0);

        tokenActive = true;
        TokenActivated();
    }

    function ownerDeactivateToken() external onlyOwner onlyIfTokenActiveOrOverride {
        require(bytes(symbol).length > 0);

        tokenActive = false;
        TokenDeactivated();
    }
    

}

