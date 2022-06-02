# Work in progress

# Create a read-only view of data for AI applications with sensitive information masked 

In any business there is lot of sensitive information collected like date of birth, credit card number, mobile number etc. This data is stored in databases. A data governance framework must be in place to enforce the privacy and allowed use of such information.

There are many scenarios where such sensitive information is needed by other business applications. Few examples are listed here:
- In a customer support application where customer orders are being queried - the mobile number, credit card number or address must be partially masked with maybe last 4 characters displayed.
- A machine learning application needs to perform an analysis based on sensitive information like credit card number. In such cases, the machine learning application needs to be provided obfuscated data for analysis and model building.

This code pattern demonstrates a methodology to provide a read-only view of data with senstive information masked for an application. 

For this purpose, an insurance business scenario has been taken. There are two applications provided:
- An insurance portal application
- A chatbot application

A customer registers on the insurance portal. During registration, the customer provides mobile number, address and e-mail. After registration, the customer can login and purchase insurance policies. The customer supplies credit card details for purchasing the policy. After an insurance policy has been purchased, a customer can query policy details with next premium due information on the chatbot. The policy details will contain the last 4 digits of the credit card that was used to purchase the policy.

In this code pattern, you will learn how to:
- Set up data assets for governance in the Watson Knowledge Catalog
- Create data categories, classes, business terms and data protection rules for the data assets
- Create virtualized view of the data on Watson Query with data policies enforced
- Create a chatbot aapplication using Watson Assistant that consumes the read-only data with sensitive information masked from Watson Query

Security Verify has been used to implement authentication for the insurance application.

TODO - architecture diagram

## Flow

## Prerequisites
- [IBM Cloud account](https://cloud.ibm.com/)
- [Red Hat OpenShift instance](https://cloud.ibm.com/kubernetes/catalog/create?platformType=openshift)
- [Git client](https://git-scm.com/downloads)
- [The OpenShift CLI (oc)](https://cloud.ibm.com/docs/openshift?topic=openshift-openshift-cli)
- [Cloud Pak For Data](https://cloud.ibm.com/catalog/content/ibm-cp-datacore-6825cc5d-dbf8-4ba2-ad98-690e6f221701-global)
- [IBM Security Verify](https://www.ibm.com/security/digital-assets/iam/verify-demo-trial/)

## Steps
1. [Clone the repository](#1-clone-the-repository)
2. [Create IBM Cloud Services instances](#2-create-ibm-cloud-services)
3. [Configuration of services](#3-configuration-of-services)
4. [Deploy Insurance Portal Application](#4-deploy-insurance-portal-application)
5. [Create-Chatbot](#5-create-chatbot)
6. [Access the Application](#6-access-the-application)
