#!/bin/bash

read -p "email: " email
read -s -p "Password: " password

token_info=`curl -s -X POST -H "Origin: http://localhost:8080" -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=password&username=$email&password=$password" http://localhost:8080/oauth/token`
REGEX='"access_token":"(.*)","token_type"'

echo

if [[ $token_info =~ $REGEX ]]; then
  token=${BASH_REMATCH[1]}
else
  echo "Something's gone horribly wrong! Couldn't get the token!"
  exit 1;
fi

curl -H "Authorization: Bearer $token" http://localhost:8080/accounts
