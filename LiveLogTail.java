/*
Logs Livetail
In Datadog, the live tail feature allows users to view logs in real-time, as they are being generated. It also allows you to match logs based on a query. You can enter specific search terms, and the live tail will only display the logs that match these criteria.

Inputs
The input list contains strings that start with either “Q:” or “L:”. The live tail will need to match logs to previous queries.

Queries are prefixed with “Q: “, and consist of a case insensitive list of words which must all match.

Logs are prefixed with “L: “.

livetail_stream = [
  "Q: database",
  "Q: Stacktrace",
  "Q: loading failed",
  "L: Database service started",
  "Q: snapshot loading",
  "Q: fail",
  "L: Started processing events",
  "L: Loading main DB snapshot",
  "L: Loading snapshot failed no stacktrace available",
]
Outputs
The live tail outputs a list containing strings that start with either “ACK:” or “M:”.

“ACK:” represents an acknowledgment of a query. Queries are given unique IDs.

“M:” represents a response with successful matches. These indicate which query IDs matched the given log.

Given the input above, the expected output should be similar to the following:

livetail_output = [
  "ACK: database; ID=1",
  "ACK: Stacktrace; ID=2",
  "ACK: loading failed; ID=3",
  "M: Database service started; Q=1",
  "ACK: snapshot loading; ID=4",
  "ACK: fail; ID=5",
  "M: Loading main DB snapshot; Q=4",
  "M: Loading snapshot failed no stacktrace available; Q=2,3,4",
]

*/

List<String> processLogs(List<String> input) {
    List<String> output = new ArrayList<>();
    Map<Integer, List<String>> queries = new LinkedHashMap<>();
    int queryId = 1;

    for (String line : input) {
        if (line.startsWith("Q: ")) {
            String query = line.substring(3);
            List<String> words = Arrays.asList(query.toLowerCase().split("\\s+"));
            queries.put(queryId, words);
            output.add("ACK: " + query + "; ID=" + queryId);
            queryId++;
        } else if (line.startsWith("L: ")) {
            String log = line.substring(3);
            String logLower = log.toLowerCase();
            List<Integer> matchedIds = new ArrayList<>();

            for (Map.Entry<Integer, List<String>> entry : queries.entrySet()) {
                int id = entry.getKey();
                List<String> words = entry.getValue();
                boolean allMatch = true;

                for (String word : words) {
                    if (!logLower.contains(word)) {
                        allMatch = false;
                        break;
                    }
                }

                if (allMatch) {
                    matchedIds.add(id);
                }
            }

            if (!matchedIds.isEmpty()) {
                String idsStr = matchedIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                output.add("M: " + log + "; Q=" + idsStr);
            }
        }
    }

    return output;
}
