/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-07T14:37:25.738+08:00
 */

package com.demo.server.common.utils.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShellUtil {
    private static final Logger logger = LoggerFactory.getLogger(ShellUtil.class);

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private ShellUtil() {
        throw new AssertionError();
    }

    /**
     * check whether has root permission
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    public static CommandResult execCommand(String command) {
        return execCommand(new String[]{command}, false, true);
    }

    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot, true);
    }

    public static CommandResult execCommand(List<String> commands) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), false, true);
    }

    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
    }

    public static CommandResult execCommand(String[] commands) {
        return execCommand(commands, false, true);
    }

    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands        command array
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        logger.info("execCommand isRoot=[{}],commands=[{}]", isRoot, Arrays.stream(commands).collect(Collectors.joining("; ")));

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);

            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();

            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                    successMsg.append("\n");
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                    errorMsg.append("\n");
                }
            }
        } catch (IOException e) {
            logger.error("execCommand error. isRoot=[{}],commands=[{}]", isRoot, Arrays.stream(commands).collect(Collectors.joining("; ")), e);
        } catch (Exception e) {
            logger.error("execCommand error. isRoot=[{}],commands=[{}]", isRoot, Arrays.stream(commands).collect(Collectors.joining("; ")), e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                logger.error("execCommand error. isRoot=[{}],commands=[{}]", isRoot, Arrays.stream(commands).collect(Collectors.joining("; ")), e);
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * result of command, 0 means normal, else means error
     */
    public static class CommandResult {
        public int result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        public int getResult() {
            return result;
        }

        public String getSuccessMsg() {
            return successMsg;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

    public static void main(String[] args) {
        System.out.println("checkRootPermission=" + ShellUtil.checkRootPermission());

        CommandResult result = ShellUtil.execCommand("ifconfig");

        System.out.println(result.result);
        System.out.println(result.successMsg);
        System.out.println(result.errorMsg);

        result = ShellUtil.execCommand(new String[]{"cd " + JvmUtil.getJavaHomePath(), "pwd", "ls -al"});
        System.out.println(result.result);
        System.out.println(result.successMsg);
        System.out.println(result.errorMsg);
    }
}
