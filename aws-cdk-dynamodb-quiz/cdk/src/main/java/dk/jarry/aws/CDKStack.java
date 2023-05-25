package dk.jarry.aws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.ITable;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class CDKStack extends Stack {

	static String functionName = "dk_jarry_lambda_quiz_boundary_quizs";
	static String lambdaHandler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";
	static int memory = 512;
	static int maxConcurrency = 2;
	static int timeout = 10;

	public CDKStack(final Construct scope, final String id, final StackProps props) {
		super(scope, id, props);

		Map<String, String> configuration = new HashMap<>();

		String dynamoDbTableName = id + "-quizs";
		var table = createTable(id, dynamoDbTableName);
		Tags.of(table).add("environment", "demo");
		configuration.put("dynamoDbTableName", table.getTableName());

		String roleName = id + "-quiz-role";
		IRole lambdaRole = createRole(id, roleName);
		Tags.of(lambdaRole).add("environment", "demo");

		var function = createFunction(functionName, lambdaHandler, configuration, memory, maxConcurrency, timeout,
				lambdaRole);
		Tags.of(function).add("environment", "development");

		CfnOutput.Builder.create(this, "FunctionArn").value(function.getFunctionArn()).build();

		integrateWithHTTPApiGateway(function);

	}

	/**
	 * https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api.html
	 */
	void integrateWithHTTPApiGateway(IFunction function) {
		var lambdaIntegration = HttpLambdaIntegration.Builder.create("HttpApiGatewayIntegration", function).build();
		var httpApiGateway = HttpApi.Builder.create(this, "HttpApiGatewayIntegration")
				.defaultIntegration(lambdaIntegration).build();
		var url = httpApiGateway.getUrl();
		url = url.substring(0, url.length() - 1);
		CfnOutput.Builder.create(this, "HttpApiGatewayUrlOutput").value(url).build();
		CfnOutput.Builder.create(this, "HttpApiGatewayCurlOutput").value("curl -i " + url + "/quizs").build();
		CfnOutput.Builder.create(this, "HttpApiGatewayTestOutput").value("../create_quiz.sh " + url + "/quizs").build();
		CfnOutput.Builder.create(this, "HttpApiGatewaySTOutput")
				.value("mvn compile quarkus:dev -Dquarkus.rest-client.extensions-api.url=" + url).build();
	}

	ITable createTable(String id, String tableName) {

		return Table.Builder.create(this, "quizs-in-stack") //
				.tableName(tableName) //
				.partitionKey(Attribute.builder()
				.name("uuid") //
				.type(AttributeType.STRING).build()) //
				.readCapacity(1) //
				.writeCapacity(1) //
				.billingMode(BillingMode.PROVISIONED) //
				.build();

	}

	Function createFunction(String functionName, String functionHandler, Map<String, String> configuration, int memory,
			int maximumConcurrentExecution, int timeout, IRole lambdaRole) {

		return Function.Builder.create(this, functionName) //
				.runtime(Runtime.JAVA_11) //
				.code(Code.fromAsset("../target/function.zip")) //
				.handler(functionHandler) //
				.memorySize(memory) //
				.functionName(functionName) //
				.environment(configuration) //
				.timeout(Duration.seconds(timeout)) //
				.reservedConcurrentExecutions(maximumConcurrentExecution) //
				.role(lambdaRole) //
				.build();
	}

	IRole createRole(final String id, String roleName) {

		Role lambdaRole = Role.Builder.create(this, "Quiz-Lambda-Role") //
				.assumedBy(new ServicePrincipal("lambda.amazonaws.com")) //
				.description("Quiz role ... with policy to use DynamoDb and Logs") //
				.roleName(roleName) //
				.build();

		lambdaRole.addToPolicy( //
				PolicyStatement.Builder.create() // Restrict to listing and describing tables
						.actions( //
								List.of( //
										"dynamodb:DescribeTable", //
										"dynamodb:ListTables", //
										"dynamodb:PutItem", //
										"dynamodb:GetItem", //
										"dynamodb:UpdateItem", //
										"dynamodb:DeleteItem", //
										"dynamodb:Scan", //
										"logs:CreateLogGroup", //
										"logs:CreateLogStream", //
										"logs:PutLogEvents") //
						) //
						.resources(List.of("*")) //
						.build());

		return lambdaRole;
	}

}
