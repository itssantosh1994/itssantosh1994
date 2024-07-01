package JsonPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;




public class TwoTesting {
	public static void main(String args[]) {
		String file1Path = System.getProperty("user.dir") + "\\target\\1.json";
		String file2Path = System.getProperty("user.dir") + "\\target\\2.json";
		String outputPath = System.getProperty("user.dir") + "\\target\\modified_file1.json";
		try {
			JSONArray jsonArray1 = readJsonFile(file1Path);
			JSONArray jsonArray2 = readJsonFile(file2Path);

			List<JSONObject> list1 = jsonArrayToList(jsonArray1);
			List<JSONObject> list2 = jsonArrayToList(jsonArray2);

			for (JSONObject jsonObject2 : list2) {

				JSONArray customJsonArray2 = jsonObject2.getJSONArray("elements");

				List<JSONObject> customList2 = jsonArrayToList(customJsonArray2);

				for (int j = 1; j < customJsonArray2.length(); j += 2) {

					JSONObject rerunJsonObject = customList2.get(j);

					String status2 = rerunJsonObject.optJSONArray("after").getJSONObject(0).getJSONObject("result")
							.optString("status");
					String lineNo2 = rerunJsonObject.optString("line");
					String failedtestCase = rerunJsonObject.optString("name");

					System.out.println(
							"Rerun-Test Status: " + status2 + " Line No: " + lineNo2 + " Name: " + failedtestCase);

					failedtestCase = failedtestCase.concat(lineNo2);
					boolean insideloop = false;

					for (int k = 0; k < list1.size(); k++) {

						JSONArray customJsonArray1 = list1.get(k).getJSONArray("elements");

						List<JSONObject> customList1 = jsonArrayToList(customJsonArray1);

						for (int i = 1; i < customJsonArray1.length(); i += 2) {

							JSONObject insideJsonObject = customList1.get(i);

							String status = insideJsonObject.optJSONArray("after").getJSONObject(0)
									.getJSONObject("result").optString("status");

							String lineNo = insideJsonObject.optString("line");

							String testCase = insideJsonObject.optString("name");

							System.out.println("Test Status: " + status + " Line No: " + lineNo + " Name: " + testCase);

							testCase = testCase.concat(lineNo);

							if (failedtestCase.equalsIgnoreCase(testCase)) {
								System.out.println("Removed Test Case: " + testCase);
								System.out.println("failedtestCase: " + failedtestCase);
								System.out.println("Removing JSON Object from First JSON ");
								customList1.set(i, rerunJsonObject);

								status = customList1.get(i).optJSONArray("after").getJSONObject(0)
										.getJSONObject("result").optString("status");

								lineNo = customList1.get(i).optString("line");

								testCase = customList1.get(i).optString("name");

								System.out.println("After Remove Details in JSON1: " + status + " Line No: " + lineNo
										+ " Name: " + testCase);

								insideloop = true;
								break;

							}
						}
						if (insideloop) {
							// Update list1 with the modified customList1
							list1.get(k).put("elements", listToJSONArray(customList1));
							break;
						}

					}
				}
			}

			System.out.println("namesInFile1: ");

			// Getting a Size after remove

			for (JSONObject jsonObject1 : list1) {

				JSONArray customJsonArray2 = jsonObject1.getJSONArray("elements");

				for (int j = 1; j < customJsonArray2.length(); j += 2) {

					String status2 = jsonObject1.getJSONArray("elements").optJSONObject(j).optJSONArray("after")
							.getJSONObject(0).getJSONObject("result").optString("status");
					String lineNo2 = jsonObject1.getJSONArray("elements").getJSONObject(j).optString("line");
					String failedtestCase = jsonObject1.getJSONArray("elements").getJSONObject(j).optString("name");

					System.out.println(
							"After Merging Status: " + status2 + " Line No: " + lineNo2 + " Name: " + failedtestCase);
				}
			}

			// Convert list1 back to JSONArray and write to output file
			JSONArray resultArray = listToJSONArray(list1);
			writeJsonFile(outputPath, resultArray);

			System.out.println("Filtered JSON file has been created at: " + outputPath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static JSONArray readJsonFile(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		reader.close();
		return new JSONArray(stringBuilder.toString());
	}

	private static void writeJsonFile(String filePath, JSONArray jsonArray) throws IOException {
		FileWriter fileWriter = new FileWriter(filePath);
		fileWriter.write(jsonArray.toString(4)); // Pretty print with 4 space indentation
		fileWriter.close();
	}

	// Function to convert JSONArray to List of JSONObjects
	public static List<JSONObject> jsonArrayToList(JSONArray jsonArray) {
		List<JSONObject> list = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(jsonArray.getJSONObject(i));
		}
		return list;
	}

	// Function to convert List of JSONObjects to JSONArray
	public static JSONArray listToJSONArray(List<JSONObject> list) {
		JSONArray jsonArray = new JSONArray();
		for (JSONObject obj : list) {
			jsonArray.put(obj);
		}
		return jsonArray;
	}

}
