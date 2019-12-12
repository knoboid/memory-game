import  rest from 'rest';
import  defaultRequest from 'rest/interceptor/defaultRequest';
import  mime from 'rest/interceptor/mime';
import  errorCode from 'rest/interceptor/errorCode';
import baseRegistry from 'rest/mime/registry';

const registry = baseRegistry.child();

const client = rest
		.wrap(mime, { registry: registry })
		.wrap(errorCode)
		.wrap(defaultRequest, { headers: { 'Accept': 'application/json' }});


export default client;
