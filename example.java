public void applyTableBatches(TableRequest tableRequest, Consumer<JCoTable> consumer) throws JCoException {
        int batchSize = tableRequest.batchSize == -1? 1000000 : tableRequest.batchSize;
        int rowsToSkip = 0;
        while (tableRequest.maxRows == -1 || rowsToSkip < tableRequest.maxRows) {
            JCoFunction function = getTableFunction(tableRequest);
            function.getImportParameterList().setValue("ROWSKIPS", rowsToSkip);
            function.getImportParameterList().setValue("ROWCOUNT", batchSize);
            function.execute(destination);
            JCoTable resultTable = function.getTableParameterList().getTable("DATA");
            if (resultTable.getNumRows() == 0) {
                break;
            }
            consumer.accept(resultTable);
            rowsToSkip += batchSize;
        }
    }
