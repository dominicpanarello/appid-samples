#!/usr/bin/env python
import requests
import json
import argparse

def get_from_api(path, token, isConfig=True):
    headers = {'Authorization': token, 'Accept': 'application/json'}
    configPath = "/config" if isConfig else ""
    url = src_management_url + src_tenantId + configPath + "/" +path
    return requests.get(
        url,
        headers=headers); 

def put_to_api(path, content, token, isConfig=True):
    headers = {'Authorization': token, 'Accept': 'application/json', 'Content-Type': 'application/json'}
    configPath = "/config" if isConfig else ""
    url = tgt_management_url + trgt_tenantId + configPath + "/" + path
    return requests.put(
        url,
        data=content,
        headers=headers);
    
def post_to_api(path, content, token, isConfig=True):
    headers = {'Authorization': token, 'Accept': 'application/json', 'Content-Type': 'application/json'}
    configPath = "/config" if isConfig else ""
    url = tgt_management_url + trgt_tenantId + configPath + "/" + path
    return requests.post(
        url,
        data=content,
        headers=headers);    

def get_iam_token():
    headers = {'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json'}
    data = 'grant_type=urn:ibm:params:oauth:grant-type:apikey&apikey=' + apiKey;

    r = requests.post(iam_url + ".bluemix.net/oidc/token", data=data, headers=headers);
    return 'Bearer ' + json.loads(r.text)['access_token'];

def copy(path, token, isConfig=True):
    r = get_from_api(path, token, isConfig)
    debug(r.status_code)
    debug(r.content)
    if 200 <= r.status_code < 300:
        print("Success! got " + path + " from source")
    else:
        print("Failed to get " + path + " from source")
    jcontent = r.content

    r = put_to_api(path, jcontent, token)
    debug(r.status_code)
    debug(r.text)
    if 200 <= r.status_code < 300:
        print("Success! put to " + path + " at target")
    else:
        print("Failed to put to " + path + " at target")

def copyTemplates(token):
    copy("cloud_directory/templates/" + "USER_VERIFICATION", token)
    copy("cloud_directory/templates/" + "RESET_PASSWORD", token)
    copy("cloud_directory/templates/" + "WELCOME", token)
    copy("cloud_directory/templates/" + "PASSWORD_CHANGED", token)

def copyActions(token):
    copy("cloud_directory/action_url/" + "on_user_verified", token)
    copy("cloud_directory/action_url/" + "on_reset_password", token)
    
def copyCloudUsers(token):
    path = "cloud_directory/Users"
    users = get_from_api(path, token, False)
    data = json.loads(users.content)
    resources = data['Resources']
    for user in resources:
      debug("Processing user " + user['displayName'])
      userProfileValue = get_user_profile_value(user['id'], token)
      if is_user_attribute_filter() and not userProfileValue == attrValue:
	print("Skipping user with incorrect " + attrName + " : is " + str(userProfileValue) + " expected " + attrValue)
	continue
      
      user['password'] = 'mypassword'
      r = post_to_api(path, json.dumps(user), token, False)
      debug(r.status_code)
      debug(r.text)
      if 200 <= r.status_code < 300:
	print("Success! put to " + path + " at target")
      else:
	print("Failed to put to " + path + " at target: " + str(r.status_code))
	
def get_user_profile_value(userId, token):
    if not is_user_attribute_filter():
      return
    
    user = get_from_api("users?id="+userId, token, False)
    userData = json.loads(user.content)['users']
    if not userData:
      return
    
    userProfileId = userData[0]['id']
    if (userProfileId is not None):
      profile = get_from_api("users/" + userProfileId + "/profile", token, False)
      profileData = json.loads(profile.content)['attributes']
      if profileData:
	return profileData.get(attrName, None)
      
    return

def is_user_attribute_filter():
  return (attrName is not None) and (attrValue is not None)
	
def debug(str):
    if verbose:
        print(str)
        


def main():
    parser = argparse.ArgumentParser(description='Copy configuration from one App ID instance to another')
    parser.add_argument('source', type=str,
                        help='The App ID instance id (tenantID) source of configuration')

    parser.add_argument('target', type=str,
                        help='The App ID instance id (tenantID) target to configure')

    parser.add_argument('-k', '--apikey', type=str,
                        help='API Key for creating an IAM token with sufficient privileges')

    parser.add_argument('-r', '--region', type=str, default='us-south',
                        help='IBM Cloud region for source instance. It will also be the target region, unless specified otherwise')

    parser.add_argument('-R', '--target_region', type=str,
                        help='IBM Cloud region for source instance, if different then target')

    parser.add_argument('-v', '--verbose', action='store_true',
                        help='Run with verbose mode. REST messages content will be displayed')
    
    parser.add_argument('-a', '--attr_name', type=str,
                        help='User profile attribute name for user selection')
    parser.add_argument('-l', '--attr_value', type=str,
                        help='User profile attribute value for user selection')

    args = parser.parse_args()

    global region
    global src_tenantId
    global trgt_tenantId
    global apiKey
    global iam_url
    global src_management_url
    global tgt_management_url
    global verbose
    global attrName
    global attrValue

    src_tenantId = args.source
    trgt_tenantId = args.target
    apiKey = args.apikey
    region = args.region
    tgt_region = args.target_region
    verbose = args.verbose
    attrName = args.attr_name
    attrValue = args.attr_value

    if region == "us-south":
        region = 'ng'

    if not tgt_region:
        tgt_region=region

    if tgt_region == "us-south":
        tgt_region = 'ng'

    iam_url = "https://iam." + region
    src_management_url = "https://appid-management." + region + ".bluemix.net/management/v4/"
    tgt_management_url = "https://appid-management." + tgt_region + ".bluemix.net/management/v4/"
    debug("source:" + src_management_url)
    debug("target:" + tgt_management_url)
    debug("attrName:" + str(attrName))
    debug("attrValue:" + str(attrValue))
    
    token = get_iam_token()

    copy("idps/facebook", token)
    copy("idps/google", token)
    copy("idps/cloud_directory", token)
    # copy("idps/saml", token)
    #
    copy("tokens", token)
    # copy("redirect_uris", token)
    copy("users_profile", token)
    # copy("ui/theme_color", token)
    # copy("ui/media", token)
    # copy("cloud_directory/sender_details", token)

    copyTemplates(token)
    #copyActions(token)
    copyCloudUsers(token)


if __name__ == "__main__":
    main()
