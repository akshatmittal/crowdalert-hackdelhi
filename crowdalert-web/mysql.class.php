<?php
// require("config.php");
// MySQL Class

class MySQL {

	// Base variables

	var $sLastError;		// Holds the last error

	var $sLastQuery;		// Holds the last query

	var $aResult;			// Holds the MySQL query result

	var $iRecords;			// Holds the total number of records returned

	var $iAffected;			// Holds the total number of records affected

	var $aRawResults;		// Holds raw 'arrayed' results

	var $aArrayedResult;	// Holds a single 'arrayed' result

	var $aArrayedResults;	// Holds multiple 'arrayed' results (usually with a set key)

	

	var $sHostname="localhost";	// MySQL Hostname

	var $sUsername="root";	// MySQL Username

	var $sPassword="";			// var $sPassword = "wtf6tFYd";	// MySQL Password

	var $sDatabase="bheed";	// MySQL Database

	

	var $sDBLink;			// Database Connection Link

	

	// Class Constructor
	
	// Assigning values to variables

	public function MySQL(){

		$this->Connect();

	}

	

	// Connects class to database

	// $bPersistant (boolean) - Use persistant connection?

	private function Connect($bPersistant = false){

		if($this->sDBLink){

			mysql_close($this->sDBLink);

		}

		

		if($bPersistant){

			$this->sDBLink = mysql_pconnect($this->sHostname, $this->sUsername, $this->sPassword);

		}else{

			$this->sDBLink = mysql_connect($this->sHostname, $this->sUsername, $this->sPassword);

		}

		if (!$this->sDBLink){

   			$this->sLastError = 'Could not connect to server: ' . mysql_error();

			return false;

		}

		

		if(!$this->UseDB()){

			$this->sLastError = 'Could not connect to database: ' . mysql_error();

			return false;

		}

		return true;

	}

	

	// Select database to use

	private function UseDB(){

		if (!mysql_select_db($this->sDatabase)) {

			$this->sLastError ='Cannot select database: ' . mysql_error();

			return false;

		}else{

			return true;

		}

	}

	

	// Executes MySQL query

	public function ExecuteSQL($sSQLQuery){

		$this->sLastQuery 	= $sSQLQuery;

		if($this->aResult 		= mysql_query($sSQLQuery)){

			$this->iRecords 	= @mysql_num_rows($this->aResult);

			$this->iAffected	= @mysql_affected_rows();

			return true;

		}else{

			$this->sLastError = mysql_error();

			return false;

		}

	}

	

	// Adds a record to the database

	// based on the array key names

	public function Insert($sTable, $aVars, $aExclude = ''){

		// Catch Exceptions

		if($aExclude == ''){

			$aExclude = array();

		}

		

		array_push($aExclude, 'MAX_FILE_SIZE');

		

		// Prepare Variables

		$aVars = $this->SecureData($aVars);

		

		$sSQLQuery = 'INSERT INTO `' . $sTable . '` SET ';

		foreach($aVars as $iKey=>$sValue){

			if(in_array($iKey, $aExclude)){

				continue;

			}

			$sSQLQuery .= '`' . $iKey . '` = "' . $sValue . '", ';

		}

		

		$sSQLQuery = substr($sSQLQuery, 0, -2);

		

		if($this->ExecuteSQL($sSQLQuery)){

			return true;

		}else{

			return false;

		}

	}

	

	// Deletes a record from the database

	public function Delete($sTable, $aWhere='', $sLimit='', $bLike=false){

		$sSQLQuery = 'DELETE FROM `' . $sTable . '` WHERE ';

		if(is_array($aWhere) && $aWhere != ''){

			// Prepare Variables

			$aWhere = $this->SecureData($aWhere);

			

			foreach($aWhere as $iKey=>$sValue){

				if($bLike){

					$sSQLQuery .= '`' . $iKey . '` LIKE "%' . $sValue . '%" AND ';

				}else{

					$sSQLQuery .= '`' . $iKey . '` = "' . $sValue . '" AND ';

				}

			}

			

			$sSQLQuery = substr($sSQLQuery, 0, -5);

		}

		

		if($sLimit != ''){

			$sSQLQuery .= ' LIMIT ' .$sLimit;

		}

		

		if($this->ExecuteSQL($sSQLQuery)){

			return true;

		}else{

			return false;

		}

	}

	

	// Gets a single row from $1

	// where $2 is true

	public function Select($sFrom, $aWhat='*', $aWhere='', $sOrderBy='', $sLimit='', $bLike=false, $sOperand='AND'){

		// Catch Exceptions

		if(trim($sFrom) == ''){

			return false;

		}

		

		$sSQLQuery = 'SELECT ';

		

		if($aWhat != '*'){

			$aWhat = $this->SecureData($aWhat);

			foreach($aWhat as $sValue)

				$sSQLQuery .= '`' . $sValue . '`, ';

			$sSQLQuery = substr($sSQLQuery, 0, -2);

		}else

			$sSQLQuery .= '*';

		

		$sSQLQuery .= ' FROM `' . $sFrom . '` WHERE ';

		

		if(is_array($aWhere) && $aWhere != ''){

			// Prepare Variables

			$aWhere = $this->SecureData($aWhere);

			

			foreach($aWhere as $iKey=>$sValue){

				if($bLike){

					$sSQLQuery .= '`' . $iKey . '` LIKE "%' . $sValue . '%" ' . $sOperand . ' ';

				}else{

					$sSQLQuery .= '`' . $iKey . '` = "' . $sValue . '" ' . $sOperand . ' ';

				}

			}

			

			$sSQLQuery = substr($sSQLQuery, 0, -5);



		}else{

			$sSQLQuery = substr($sSQLQuery, 0, -7);

		}

		

		if($sOrderBy != ''){

			$sSQLQuery .= ' ORDER BY ' .$sOrderBy;

		}

		

		if($sLimit != ''){

			$sSQLQuery .= ' LIMIT ' .$sLimit;

		}

		

		if($this->ExecuteSQL($sSQLQuery)){

			return true;

		}else{

			return false;

		}

		

	}

	

	// Updates a record in the database

	// based on WHERE

	public function Update($sTable, $aSet, $aWhere, $aExclude = ''){

		// Catch Exceptions

		if(trim($sTable) == '' || !is_array($aSet) || !is_array($aWhere)){

			return false;

		}

		if($aExclude == ''){

			$aExclude = array();

		}

		

		array_push($aExclude, 'MAX_FILE_SIZE');

		

		$aSet 	= $this->SecureData($aSet);

		$aWhere = $this->SecureData($aWhere);

		

		// SET

		

		$sSQLQuery = 'UPDATE `' . $sTable . '` SET ';

		

		foreach($aSet as $iKey=>$sValue){

			if(in_array($iKey, $aExclude)){

				continue;

			}

			$sSQLQuery .= '`' . $iKey . '` = "' . $sValue . '", ';

		}

		

		$sSQLQuery = substr($sSQLQuery, 0, -2);

		

		// WHERE

		

		$sSQLQuery .= ' WHERE ';

		

		foreach($aWhere as $iKey=>$sValue){

			$sSQLQuery .= '`' . $iKey . '` = "' . $sValue . '" AND ';

		}

		

		$sSQLQuery = substr($sSQLQuery, 0, -5);

		

		if($this->ExecuteSQL($sSQLQuery)){

			return true;

		}else{

			return false;

		}

	}

	

	// 'Arrays' a single result

	public function ArrayResult(){

		if($this->iAffected){

			$this->aArrayedResult = @mysql_fetch_assoc($this->aResult) or die (mysql_error());

			return $this->aArrayedResult;

		}

		return false;

	}

	

	public function ArrayResults(){

		if(isset($this->aArrayedResults))

			unset($this->aArrayedResults);

		$this->aArrayedResults = array();

		$x = 0;

		while($aRow = mysql_fetch_assoc($this->aResult)){

			$this->aArrayedResults[$x] = $aRow;

			$x++;

		}

		return $this->aArrayedResults;

	}

	

	// 'Arrays' multiple results with a key

	public function ArrayResultsWithKey($sKey='id'){

		if(isset($this->aArrayedResults)){

			unset($this->aArrayedResults);

		}

		$this->aArrayedResults = array();

		while($aRow = mysql_fetch_assoc($this->aResult)){

			foreach($aRow as $sTheKey => $sTheValue){

				$this->aArrayedResults[$aRow[$sKey]][$sTheKey] = $sTheValue;

			}

		}

		return $this->aArrayedResults;

	}

	

	// Performs a 'mysql_real_escape_string' on the entire array/string

	public function SecureData($aData){

		if(is_array($aData)){

			foreach($aData as $iKey=>$sVal){

				if(!is_array($aData[$iKey])){

					$aData[$iKey] = mysql_real_escape_string($aData[$iKey]);

				}

			}

		}else{

			$aData = mysql_real_escape_string($aData);

		}

		return $aData;

	}

}

?>