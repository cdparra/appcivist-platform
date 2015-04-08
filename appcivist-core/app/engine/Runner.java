package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.services.Service;
import models.services.ServiceAuthentication;
import models.services.ServiceOperation;
import models.services.ServiceOperationDefinition;
import models.services.ServiceParameter;
import models.services.ServiceParameterDefinition;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;

public class Runner {

	public static void execute(ServiceOperation op) {
		ServiceOperationDefinition opDef = op.getDefinition();
		Service opService = op.getService();
		
		String serviceUrl = opService.getBaseUrl();
		
		List<String> pathParams = new ArrayList<String>();
		Map<String, String> otherHeaders = new HashMap<String, String>();
		Map<String, String> queryParams = new HashMap<String, String>();

		String servicePath = "";
		//String serviceQuery = "";
		String serviceBody = "";
		
		List<ServiceParameter> pValues = op.getParameters();
		
		for (ServiceParameter p : pValues) {
			String paramType = p.getServiceParameter().getType();
			ServiceParameterDefinition pDef = p.getServiceParameter(); 
			String value = p.getValue();
			if (paramType.equals("BODY_PARAM")) {
				serviceBody = value;
				if (pDef.getDataType().equals("JSON")) {
					otherHeaders.put("Content-Type", "application/json");
				}
			} else if (paramType.equals("PATH_PARAM")) {
				String path = "/"+value;
				pathParams.add(pDef.getPathOrder(), path);
			} else if (paramType.equals("QUERY_PARAM")) {
				queryParams.put(pDef.getName(),value);
			}
		}
		
		for (String path : pathParams) {
			servicePath+=path;
		}

		// You end by calling a method corresponding to the HTTP method you want
		// to use.
		// This ends the chain, and uses all the options defined on the built
		// request in the
		// WSRequestHolder.

		
		String method = opDef.getMethod();
		String opName = opDef.getName();
		Boolean nameOnPath = opDef.getNameOnPath();
		// TODO: Use op_type to either call a REST service or another type
		String opType = op.getDefinition().getType(); 

		if (nameOnPath) {
			serviceUrl += "/" + opName;
		}
		
		if (opService.getTrailingSlash()) {
			servicePath+="/";
		}
		
		WSRequestHolder holder = WS.url(serviceUrl+servicePath);
		
		List<ServiceAuthentication> authHeaders = opService.getAuth();

		for (ServiceAuthentication serviceAuth : authHeaders) {
			String injection = serviceAuth.getTokenInjection();
			if (injection.equals("Cookie")) {
				holder.setHeader("Cookie", 
									serviceAuth.getTokenParamName()
									+ "=" 
									+ serviceAuth.getToken());
//				holder.setHeader(serviceAuth.getTokenParamName(),
//						serviceAuth.getToken());
//				holder.setAuth(serviceAuth.getToken());
			} else if (injection.equals("HEADER")){
				holder.setHeader(serviceAuth.getTokenParamName(), serviceAuth.getToken());
			}
		}
	
		for (String key : otherHeaders.keySet()) {
			holder.setHeader(key, otherHeaders.get(key));
		}

		for (String key : queryParams.keySet()) {
			holder.setQueryParameter(key, queryParams.get(key));
		}
		
		holder.setMethod(method);
		//holder.setQueryString(serviceQuery);
		holder.setBody(serviceBody);

		System.out
				.println("RUNNER > #1 > Request with following parameters "
							+ "\n=========> serviceUrl="+serviceUrl
							+ "\n=========> servicePath="+servicePath
							+ "\n=========> serviceQuery="+queryParams.toString()
							+ "\n=========> serviceMethod="+method
							+ "\n=========> serviceBody="+serviceBody
							+ "\n=========> serviceHeaders="+holder.getHeaders().toString()
						);
		
		System.out
				.println("RUNNER > #1 > Executing the request: "
							+ holder.toString());
		
		Promise<JsonNode> responsePromise = holder.execute().map(
				new Function<WSResponse, JsonNode>() {
					public JsonNode apply(WSResponse response) {
						JsonNode json = response.asJson();
						System.out
								.println("RUNNER v1 > #4.1 > Received this response from operation execution:"
										+ json.toString());
						return json;
					}
				});
	}

}
