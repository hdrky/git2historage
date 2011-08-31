package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JavaPredefinedModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedInstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVClassMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVFileMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVMethodMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeNameResolver;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeParser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.DefaultPluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.DefaultPluginLoader;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.PluginLoadException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.objectweb.asm.ClassReader;


/**
 * 
 * @author higo
 * 
 * MetricsTool�̃��C���N���X�D ���݂͉������D
 * 
 * since 2006.11.12
 * 
 */
public class MetricsTool {

    /**
     * 
     * @param args �Ώۃt�@�C���̃t�@�C���p�X
     * 
     * ���݉������D �Ώۃt�@�C���̃f�[�^���i�[������C�\����͂��s���D
     */
    public static void main(String[] args) {

        initSecurityManager();

        // ���\���p�̃��X�i���쐬
        final MessageListener outListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        final MessageListener errListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.err.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(errListener);

        final Options options = new Options();

        {
            final Option h = new Option("h", "help", false, "display usage");
            h.setRequired(false);
            options.addOption(h);
        }

        {
            final Option v = new Option("v", "verbose", false, "output progress verbosely");
            v.setRequired(false);
            options.addOption(v);
        }

        {
            final Option d = new Option("d", "directores", true,
                    "specify target directories (separate with comma \',\' if you specify multiple directories");
            d.setArgName("directories");
            d.setArgs(1);
            d.setRequired(false);
            options.addOption(d);
        }

        {
            final Option i = new Option(
                    "i",
                    "input",
                    true,
                    "specify the input that contains the list of target files (separate with comma \',\' if you specify multiple inputs)");
            i.setArgName("input");
            i.setArgs(1);
            i.setRequired(false);
            options.addOption(i);
        }

        {
            final Option l = new Option("l", "language", true, "specify programming language");
            l.setArgName("input");
            l.setArgs(1);
            l.setRequired(false);
            options.addOption(l);
        }

        {
            final Option m = new Option("m", "metrics", true,
                    "specify measured metrics with comma separeted format (e.g., -m rfc,dit,lcom)");
            m.setArgName("metrics");
            m.setArgs(1);
            m.setRequired(false);
            options.addOption(m);
        }

        {
            final Option F = new Option("F", "FileMetricsFile", true,
                    "specify file that measured FILE metrics were stored into");
            F.setArgName("file metrics file");
            F.setArgs(1);
            F.setRequired(false);
            options.addOption(F);
        }

        {
            final Option C = new Option("C", "ClassMetricsFile", true,
                    "specify file that measured CLASS metrics were stored into");
            C.setArgName("class metrics file");
            C.setArgs(1);
            C.setRequired(false);
            options.addOption(C);
        }

        {
            final Option M = new Option("M", "MethodMetricsFile", true,
                    "specify file that measured METHOD metrics were stored into");
            M.setArgName("method metrics file");
            M.setArgs(1);
            M.setRequired(false);
            options.addOption(M);
        }

        {
            final Option A = new Option("A", "AttributeMetricsFile", true,
                    "specify file that measured ATTRIBUTE metrics were stored into");
            A.setArgName("attribute metrics file");
            A.setArgs(1);
            A.setRequired(false);
            options.addOption(A);
        }

        {
            final Option s = new Option("s", "AnalyzeStatement", false,
                    "specify this option if you don't need statement information");
            s.setRequired(false);
            options.addOption(s);
        }

        {
            final Option b = new Option("b", "libraries", true,
                    "specify libraries (.jar file or .class file or directory that contains .jar and .class files)");
            b.setArgName("libraries");
            b.setArgs(1);
            b.setRequired(false);
            options.addOption(b);
        }

        {
            final Option t = new Option("t", "threads", true,
                    "specify thread number used for multi-thread processings");
            t.setArgName("number");
            t.setArgs(1);
            t.setRequired(false);
            options.addOption(t);
        }

        final MetricsTool metricsTool = new MetricsTool();

        try {

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            // "-h"���w�肳��Ă���ꍇ�̓w���v��\�����ďI��
            // ���̂Ƃ��C���̃I�v�V�����͑S�Ė��������
            if (cmd.hasOption("h") || (0 == args.length)) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("MetricsTool", options, true);

                // -l �Ō��ꂪ�w�肳��Ă��Ȃ��ꍇ�́C��͉\����ꗗ��\��
                if (!cmd.hasOption("l")) {
                    err.println("Available languages;");
                    for (final LANGUAGE language : LANGUAGE.values()) {
                        err.println("\t" + language.getName() + ": can be specified with term \""
                                + language.getIdentifierName() + "\"");
                    }

                    // -l �Ō��ꂪ�w�肳��Ă���ꍇ�́C���̃v���O���~���O����Ŏg�p�\�ȃ��g���N�X�ꗗ��\��
                } else {
                    Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
                    err.println("Available metrics for "
                            + Settings.getInstance().getLanguage().getName());
                    metricsTool.loadPlugins(Settings.getInstance().getMetrics());
                    for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                            .getPlugins()) {
                        final PluginInfo pluginInfo = plugin.getPluginInfo();
                        if (pluginInfo.isMeasurable(Settings.getInstance().getLanguage())) {
                            err.println("\t" + pluginInfo.getMetricName());
                        }
                    }
                }

                System.exit(0);
            }

            Settings.getInstance().setVerbose(cmd.hasOption("v"));
            if (cmd.hasOption("d")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("d"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String directory = tokenizer.nextToken();
                    Settings.getInstance().addTargetDirectory(directory);
                }
            }
            if (cmd.hasOption("i")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("i"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String listFile = tokenizer.nextToken();
                    Settings.getInstance().addListFile(listFile);
                }
            }
            Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
            if (cmd.hasOption("m")) {
                Settings.getInstance().setMetrics(cmd.getOptionValue("m"));
            }
            if (cmd.hasOption("F")) {
                Settings.getInstance().setFileMetricsFile(cmd.getOptionValue("F"));
            }
            if (cmd.hasOption("C")) {
                Settings.getInstance().setClassMetricsFile(cmd.getOptionValue("C"));
            }
            if (cmd.hasOption("M")) {
                Settings.getInstance().setMethodMetricsFile(cmd.getOptionValue("M"));
            }
            if (cmd.hasOption("A")) {
                Settings.getInstance().setFieldMetricsFile(cmd.getOptionValue("A"));
            }
            Settings.getInstance().setStatement(!cmd.hasOption("s"));
            if (cmd.hasOption("b")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("b"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String library = tokenizer.nextToken();
                    Settings.getInstance().addLibrary(library);
                }
            }
            if (cmd.hasOption("t")) {
                Settings.getInstance().setThreadNumber(Integer.parseInt(cmd.getOptionValue("t")));
            }

            metricsTool.loadPlugins(Settings.getInstance().getMetrics());

            // �R�}���h���C�����������������ǂ����`�F�b�N����
            {
                // -d �� -i �̂ǂ�����w�肳��Ă���͕̂s��
                if (!cmd.hasOption("d") && !cmd.hasOption("l")) {
                    err.println("-d and/or -i must be specified in the analysis mode!");
                    System.exit(0);
                }

                // ���ꂪ�w�肳��Ȃ������͕̂s��
                if (!cmd.hasOption("l")) {
                    err.println("-l must be specified for analysis");
                    System.exit(0);
                }

                {
                    // �t�@�C�����g���N�X���v������ꍇ�� -F �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && !cmd.hasOption("F")) {
                        err.println("-F must be specified for file metrics!");
                        System.exit(0);
                    }

                    // �N���X���g���N�X���v������ꍇ�� -C �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && !cmd.hasOption("C")) {
                        err.println("-C must be specified for class metrics!");
                        System.exit(0);
                    }
                    // ���\�b�h���g���N�X���v������ꍇ�� -M �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && !cmd.hasOption("M")) {
                        err.println("-M must be specified for method metrics!");
                        System.exit(0);
                    }

                    // �t�B�[���h���g���N�X���v������ꍇ�� -A �I�v�V�������w�肳��Ă��Ȃ���΂Ȃ�Ȃ�
                    if ((0 < DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && !cmd.hasOption("A")) {
                        err.println("-A must be specified for field metrics!");
                        System.exit(0);
                    }
                }

                {
                    // �t�@�C�����g���N�X���v�����Ȃ��̂� -F�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && cmd.hasOption("F")) {
                        err.println("No file metric is specified. -F is ignored.");
                    }

                    // �N���X���g���N�X���v�����Ȃ��̂� -C�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && cmd.hasOption("C")) {
                        err.println("No class metric is specified. -C is ignored.");
                    }

                    // ���\�b�h���g���N�X���v�����Ȃ��̂� -M�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && cmd.hasOption("M")) {
                        err.println("No method metric is specified. -M is ignored.");
                    }

                    // �t�B�[���h���g���N�X���v�����Ȃ��̂� -A�@�I�v�V�������w�肳��Ă���ꍇ�͖�������|��ʒm
                    if ((0 == DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && cmd.hasOption("A")) {
                        err.println("No field metric is specified. -A is ignored.");
                    }
                }
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        final long start = System.nanoTime();

        metricsTool.analyzeLibraries();
        metricsTool.readTargetFiles();
        metricsTool.analyzeTargetFiles();
        metricsTool.launchPlugins();
        metricsTool.writeMetrics();

        out.println("successfully finished.");

        final long end = System.nanoTime();

        if (Settings.getInstance().isVerbose()) {
            out.println("elapsed time: " + (end - start) / 1000000000 + " seconds");
            out.println("number of analyzed files: "
                    + DataManager.getInstance().getFileInfoManager().getFileInfos().size());

            int loc = 0;
            for (final FileInfo file : DataManager.getInstance().getFileInfoManager()
                    .getFileInfos()) {
                loc += file.getLOC();
            }
            out.println("analyzed lines of code: " + loc);

        }
        MessagePool.getInstance(MESSAGE_TYPE.OUT).removeMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).removeMessageListener(errListener);
    }

    /**
     * ���������R���X�g���N�^�D �Z�L�����e�B�}�l�[�W���̏��������s���D
     */
    public MetricsTool() {

    }

    /**
     * ���C�u��������͂��C���̏���ExternalClassInfo�Ƃ��ēo�^����D
     * readTargetFiles()�̑O�ɌĂяo����Ȃ���΂Ȃ�Ȃ�
     */
    public void analyzeLibraries() {

        final Settings settings = Settings.getInstance();

        // java����̏ꍇ
        if (settings.getLanguage().equals(LANGUAGE.JAVA15)
                || settings.getLanguage().equals(LANGUAGE.JAVA14)
                || settings.getLanguage().equals(LANGUAGE.JAVA13)) {

            this.analyzeJavaLibraries();
        }

        else if (settings.getLanguage().equals(LANGUAGE.CSHARP)) {

        }
    }

    private void analyzeJavaLibraries() {

        final Set<JavaUnresolvedExternalClassInfo> unresolvedExternalClasses = new HashSet<JavaUnresolvedExternalClassInfo>();

        // �o�C�g�R�[�h����ǂݍ���
        for (final String path : Settings.getInstance().getLibraries()) {
            readJavaLibraries(new File(path), unresolvedExternalClasses);
        }

        // �N���X���̂��̂̂ݖ��O�����i�^�͉������Ȃ��j
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // �����N���X�͖��O�������Ȃ�
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            final String unresolvedName = unresolvedClassInfo.getName();
            final String[] name = JavaByteCodeNameResolver.resolveName(unresolvedName);
            final Set<String> unresolvedModifiers = unresolvedClassInfo.getModifiers();
            final boolean isInterface = unresolvedClassInfo.isInterface();
            final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
            for (final String unresolvedModifier : unresolvedModifiers) {
                modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
            }
            final ExternalClassInfo classInfo = unresolvedClassInfo.isInner() ? new ExternalInnerClassInfo(
                    modifiers, name, isInterface) : new ExternalClassInfo(modifiers, name,
                    isInterface);
            classInfoManager.add(classInfo);
        }

        // �O���̃N���X����ǉ�
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // �����N���X�͖���
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            // �C���i�[�N���X�łȂ��ꍇ�͖���
            if (!unresolvedClassInfo.isInner()) {
                continue;
            }

            final String[] fqName = JavaByteCodeUtility.separateName(unresolvedClassInfo.getName());
            final String[] outerFQName = Arrays.copyOf(fqName, fqName.length - 1);
            final ClassInfo outerClass = classInfoManager.getClassInfo(outerFQName);
            if (null != outerClass) { // outerClass���o�^����Ă��Ȃ���������Ȃ��̂�
                final ClassInfo classInfo = classInfoManager.getClassInfo(fqName);
                ((ExternalInnerClassInfo) classInfo).setOuterUnit(outerClass);
            }
        }

        //�@�^�p�����[�^������
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // �����N���X�͖���
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            // �܂��́C�����ς݃I�u�W�F�N�g���擾            
            final String unresolvedClassName = unresolvedClassInfo.getName();
            final String[] className = JavaByteCodeNameResolver.resolveName(unresolvedClassName);
            final ExternalClassInfo classInfo = (ExternalClassInfo) classInfoManager
                    .getClassInfo(className);

            // �^�p�����[�^������
            final List<String> unresolvedTypeParameters = unresolvedClassInfo.getTypeParameters();
            for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                TypeParameterInfo typeParameter = JavaByteCodeNameResolver.resolveTypeParameter(
                        unresolvedTypeParameter, index, classInfo);
                classInfo.addTypeParameter(typeParameter);
            }
        }

        //�@�e�N���X�ŕ\���Ă���^���������Ă���
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // �����N���X�͖��O�������Ȃ�
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            // �܂��́C�����ς݃I�u�W�F�N�g���擾            
            final String unresolvedClassName = unresolvedClassInfo.getName();
            final String[] className = JavaByteCodeNameResolver.resolveName(unresolvedClassName);
            final ExternalClassInfo classInfo = (ExternalClassInfo) classInfoManager
                    .getClassInfo(className);

            // �e�N���X,�C���^�[�t�F�[�X������
            for (final String unresolvedSuperType : unresolvedClassInfo.getSuperTypes()) {
                final ClassTypeInfo superType = (ClassTypeInfo) JavaByteCodeNameResolver
                        .resolveType(unresolvedSuperType, null, classInfo);
                classInfo.addSuperClass(superType);
                final ClassInfo superClass = superType.getReferencedClass();
                superClass.addSubClass(classInfo);
            }

            // �t�B�[���h�̉���            
            for (final JavaUnresolvedExternalFieldInfo unresolvedField : unresolvedClassInfo
                    .getFields()) {

                final String fieldName = unresolvedField.getName();
                final String unresolvedType = unresolvedField.getType();
                final TypeInfo fieldType = JavaByteCodeNameResolver.resolveType(unresolvedType,
                        null, null);
                final Set<String> unresolvedModifiers = unresolvedField.getModifiers();
                final boolean isInstance = !unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.STATIC_STRING);
                final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
                for (final String unresolvedModifier : unresolvedModifiers) {
                    modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
                }
                final ExternalFieldInfo field = new ExternalFieldInfo(modifiers, fieldName,
                        classInfo, isInstance);
                field.setType(fieldType);
                classInfo.addDefinedField(field);
            }

            // ���\�b�h�̉���
            for (final JavaUnresolvedExternalMethodInfo unresolvedMethod : unresolvedClassInfo
                    .getMethods()) {

                final String name = unresolvedMethod.getName();
                final Set<String> unresolvedModifiers = unresolvedMethod.getModifiers();
                final boolean isStatic = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.STATIC_STRING);
                final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
                for (final String unresolvedModifier : unresolvedModifiers) {
                    modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
                }

                // �R���X�g���N�^�̂Ƃ�
                if (name.equals("<init>")) {

                    final ExternalConstructorInfo constructor = new ExternalConstructorInfo(
                            modifiers);
                    constructor.setOuterUnit(classInfo);

                    // �^�p�����[�^�̉���
                    final List<String> unresolvedTypeParameters = unresolvedMethod
                            .getTypeParameters();
                    for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                        final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                        TypeParameterInfo typeParameter = (TypeParameterInfo) JavaByteCodeNameResolver
                                .resolveTypeParameter(unresolvedTypeParameter, index, constructor);
                        constructor.addTypeParameter(typeParameter);
                    }

                    // �����̉���
                    final List<String> unresolvedParameters = unresolvedMethod.getArgumentTypes();
                    for (final String unresolvedParameter : unresolvedParameters) {
                        final TypeInfo parameterType = JavaByteCodeNameResolver.resolveType(
                                unresolvedParameter, null, constructor);
                        final ExternalParameterInfo parameter = new ExternalParameterInfo(
                                parameterType, constructor);
                        constructor.addParameter(parameter);
                    }

                    // �X���[������O�̉���
                    final List<String> unresolvedThrownExceptions = unresolvedMethod
                            .getThrownExceptions();
                    for (final String unresolvedThrownException : unresolvedThrownExceptions) {
                        final TypeInfo exceptionType = JavaByteCodeNameResolver.resolveType(
                                unresolvedThrownException, null, constructor);
                        constructor.addThrownException((ReferenceTypeInfo) exceptionType);
                    }

                    classInfo.addDefinedConstructor(constructor);
                }

                // ���\�b�h�̂Ƃ�
                else {

                    final ExternalMethodInfo method = new ExternalMethodInfo(modifiers, name,
                            !isStatic);
                    method.setOuterUnit(classInfo);

                    // �^�p�����[�^�̉���
                    final List<String> unresolvedTypeParameters = unresolvedMethod
                            .getTypeParameters();
                    for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                        final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                        TypeParameterInfo typeParameter = JavaByteCodeNameResolver
                                .resolveTypeParameter(unresolvedTypeParameter, index, method);
                        method.addTypeParameter(typeParameter);
                    }

                    // �Ԃ�l�̉���
                    final String unresolvedReturnType = unresolvedMethod.getReturnType();
                    final TypeInfo returnType = JavaByteCodeNameResolver.resolveType(
                            unresolvedReturnType, null, method);
                    method.setReturnType(returnType);

                    // �����̉���
                    final List<String> unresolvedParameters = unresolvedMethod.getArgumentTypes();
                    for (final String unresolvedParameter : unresolvedParameters) {
                        final TypeInfo parameterType = JavaByteCodeNameResolver.resolveType(
                                unresolvedParameter, null, method);
                        final ExternalParameterInfo parameter = new ExternalParameterInfo(
                                parameterType, method);
                        method.addParameter(parameter);
                    }

                    // �X���[������O�̉���
                    final List<String> unresolvedThrownExceptions = unresolvedMethod
                            .getThrownExceptions();
                    for (final String unresolvedThrownException : unresolvedThrownExceptions) {
                        final TypeInfo exceptionType = JavaByteCodeNameResolver.resolveType(
                                unresolvedThrownException, null, method);
                        method.addThrownException((ReferenceTypeInfo) exceptionType);
                    }

                    classInfo.addDefinedMethod(method);
                }
            }
        }
    }

    private void readJavaLibraries(final File file,
            final Set<JavaUnresolvedExternalClassInfo> unresolvedExternalClasses) {

        try {

            // jar�t�@�C���̏ꍇ
            if (file.isFile() && file.getName().endsWith(".jar")) {

                final JarFile jar = new JarFile(file);
                for (final Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
                    final JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {

                        final ClassReader reader = new ClassReader(jar.getInputStream(entry));
                        final JavaByteCodeParser parser = new JavaByteCodeParser();
                        reader.accept(parser, ClassReader.SKIP_CODE);
                        unresolvedExternalClasses.add(parser.getClassInfo());
                    }
                }
            }

            // class�t�@�C���̏ꍇ
            else if (file.isFile() && file.getName().endsWith(".class")) {

                final ClassReader reader = new ClassReader(new FileInputStream(file));
                final JavaByteCodeParser parser = new JavaByteCodeParser();
                reader.accept(parser, ClassReader.SKIP_CODE);
                unresolvedExternalClasses.add(parser.getClassInfo());
            }

            // �f�B���N�g���̏ꍇ
            else if (file.isDirectory()) {

                for (final File subfile : file.listFiles()) {

                    if (subfile.isFile()) {
                        final String name = subfile.getName();
                        if (name.endsWith(".jar") || name.endsWith(".class")) {
                            readJavaLibraries(subfile, unresolvedExternalClasses);
                        }
                    }

                    else if (subfile.isDirectory()) {
                        readJavaLibraries(subfile, unresolvedExternalClasses);
                    }
                }
            }

            //��L�ȊO�̏ꍇ�͐������Ȃ��t�@�C����Java�̃��C�u�����Ƃ��Ďw�肳��Ă��邱�ƂɂȂ�C�I��            
            else {
                err.println("file <" + file.getAbsolutePath()
                        + "> is inappropriate as a Java library.");
                System.exit(0);
            }
        }

        // ���C�u�����̓ǂݍ��݂ŗ�O�����������ꍇ�̓v���O�������I��
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * {@link #readTargetFiles()} �œǂݍ��񂾑Ώۃt�@�C���Q����͂���.
     * 
     */
    public void analyzeTargetFiles() {

        // �Ώۃt�@�C����AST���疢�����N���X�C�t�B�[���h�C���\�b�h�����擾
        out.println("parsing all target files.");
        parseTargetFiles();

        out.println("resolving definitions and usages.");
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP1 : resolve definitions.");
        }
        resolveDefinitions();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP2 : resolve types in definitions.");
        }
        resolveTypes();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP3 : resolve method overrides.");
        }
        addOverrideRelation();
        if (Settings.getInstance().isStatement()) {
            if (Settings.getInstance().isVerbose()) {
                out.println("STEP4 : resolve field and method usages.");
            }
            addMethodInsideInfomation();
        }

        // ���@���̂���t�@�C���ꗗ��\��
        // err.println("The following files includes incorrect syntax.");
        // err.println("Any metrics of them were not measured");
        for (final TargetFile targetFile : DataManager.getInstance().getTargetFileManager()) {
            if (!targetFile.isCorrectSyntax()) {
                err.println("Incorrect syntax file: " + targetFile.getName());
            }
        }
    }

    /**
     * �v���O�C�������[�h����. �w�肳�ꂽ����C�w�肳�ꂽ���g���N�X�Ɋ֘A����v���O�C���݂̂� {@link PluginManager}�ɓo�^����.
     * null ���w�肳�ꂽ�ꍇ�͑Ώی���ɂ����Čv���\�ȑS�Ẵ��g���N�X��o�^����
     * 
     * @param metrics �w�肷�郁�g���N�X�̔z��C�w�肵�Ȃ��ꍇ��null
     */
    public void loadPlugins(final String[] metrics) {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();
        try {
            for (final AbstractPlugin plugin : (new DefaultPluginLoader()).loadPlugins()) {// �v���O�C����S���[�h
                final PluginInfo info = plugin.getPluginInfo();

                // �Ώی���Ōv���\�łȂ���Γo�^���Ȃ�
                if (!info.isMeasurable(settings.getLanguage())) {
                    continue;
                }

                if (null != metrics) {
                    // ���g���N�X���w�肳��Ă���̂ł��̃v���O�C���ƈ�v���邩�`�F�b�N
                    final String pluginMetricName = info.getMetricName();
                    for (final String metric : metrics) {
                        if (metric.equalsIgnoreCase(pluginMetricName)) {
                            pluginManager.addPlugin(plugin);
                            break;
                        }
                    }

                    // ���g���N�X���w�肳��Ă��Ȃ��̂łƂ肠�����S���o�^
                } else {
                    pluginManager.addPlugin(plugin);
                }
            }
        } catch (PluginLoadException e) {
            err.println(e.getMessage());
            System.exit(0);
        }
    }

    /**
     * ���[�h�ς݂̃v���O�C�������s����.
     */
    public void launchPlugins() {

        out.println("calculating metrics.");

        PluginLauncher launcher = new DefaultPluginLauncher();
        launcher.setMaximumLaunchingNum(1);
        launcher.launchAll(DataManager.getInstance().getPluginManager().getPlugins());

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // �C�ɂ��Ȃ�
            }
        } while (0 < launcher.getCurrentLaunchingNum() + launcher.getLaunchWaitingTaskNum());

        launcher.stopLaunching();
    }

    /**
     * {@link Settings}�Ɏw�肳�ꂽ�ꏊ�����͑Ώۃt�@�C����ǂݍ���œo�^����
     */
    public void readTargetFiles() {

        out.println("building target file list.");

        final Settings settings = Settings.getInstance();

        // �f�B���N�g������ǂݍ���
        for (final String directory : settings.getTargetDirectories()) {
            registerFilesFromDirectory(new File(directory));
        }

        // ���X�g�t�@�C������ǂݍ���
        for (final String file : settings.getListFiles()) {

            try {

                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    final String line = reader.readLine();
                    final TargetFile targetFile = new TargetFile(line);
                    targetFiles.add(targetFile);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                err.println("\"" + file + "\" is not a valid file!");
                System.exit(0);
            } catch (IOException e) {
                err.println("\"" + file + "\" can\'t read!");
                System.exit(0);
            }
        }
    }

    /**
     * ���g���N�X���� {@link Settings} �Ɏw�肳�ꂽ�t�@�C���ɏo�͂���.
     */
    public void writeMetrics() {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();

        // �t�@�C�����g���N�X���v������ꍇ
        if (0 < pluginManager.getFileMetricPlugins().size()) {

            try {
                final FileMetricsInfoManager manager = DataManager.getInstance()
                        .getFileMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getFileMetricsFile();
                final CSVFileMetricsWriter writer = new CSVFileMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("File metrics can't be output!");
            }
        }

        // �N���X���g���N�X���v������ꍇ
        if (0 < pluginManager.getClassMetricPlugins().size()) {

            try {
                final ClassMetricsInfoManager manager = DataManager.getInstance()
                        .getClassMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getClassMetricsFile();
                final CSVClassMetricsWriter writer = new CSVClassMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Class metrics can't be output!");
            }
        }

        // ���\�b�h���g���N�X���v������ꍇ
        if (0 < pluginManager.getMethodMetricPlugins().size()) {

            try {
                final MethodMetricsInfoManager manager = DataManager.getInstance()
                        .getMethodMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Method metrics can't be output!");
            }

        }

        // �t�B�[���h���g���N�X���v������ꍇ
        if (0 < pluginManager.getFieldMetricPlugins().size()) {

            try {
                final FieldMetricsInfoManager manager = DataManager.getInstance()
                        .getFieldMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Field metrics can't be output!");
            }
        }
    }

    /**
     * {@link MetricsToolSecurityManager} �̏��������s��. �V�X�e���ɓo�^�ł���΁C�V�X�e���̃Z�L�����e�B�}�l�[�W���ɂ��o�^����.
     */
    private static final void initSecurityManager() {
        try {
            // MetricsToolSecurityManager�̃V���O���g���C���X�^���X���\�z���C�������ʌ����X���b�h�ɂȂ�
            System.setSecurityManager(MetricsToolSecurityManager.getInstance());
        } catch (final SecurityException e) {
            // ���ɃZ�b�g����Ă���Z�L�����e�B�}�l�[�W���ɂ���āC�V���ȃZ�L�����e�B�}�l�[�W���̓o�^��������Ȃ������D
            // �V�X�e���̃Z�L�����e�B�}�l�[�W���Ƃ��Ďg��Ȃ��Ă��C���ʌ����X���b�h�̃A�N�Z�X����͖��Ȃ����삷��̂łƂ肠������������
            err.println("Failed to set system security manager. MetricsToolsecurityManager works only to manage privilege threads.");
        }
    }

    /**
     * 
     * @param file �Ώۃt�@�C���܂��̓f�B���N�g��
     * 
     * �Ώۂ��f�B���N�g���̏ꍇ�́C���̎q�ɑ΂��čċA�I�ɏ���������D �Ώۂ��t�@�C���̏ꍇ�́C�Ώی���̃\�[�X�t�@�C���ł���΁C�o�^�������s���D
     */
    private void registerFilesFromDirectory(final File file) {

        // �f�B���N�g���Ȃ�΁C�ċA�I�ɏ���
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                registerFilesFromDirectory(subfiles[i]);
            }

            // �t�@�C���Ȃ�΁C�g���q���Ώی���ƈ�v����Γo�^
        } else if (file.isFile()) {

            final LANGUAGE language = Settings.getInstance().getLanguage();
            final String extension = language.getExtension();
            final String path = file.getAbsolutePath();
            if (path.endsWith(extension)) {
                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
                final TargetFile targetFile = new TargetFile(path);
                targetFiles.add(targetFile);
            }

            // �f�B���N�g���ł��t�@�C���ł��Ȃ��ꍇ�͕s��
        } else {
            err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }
    }

    /**
     * �o�̓��b�Z�[�W�o�͗p�̃v�����^
     */
    protected static MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    protected static MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * �Ώۃt�@�C����AST���疢�����N���X�C�t�B�[���h�C���\�b�h�����擾
     */
    public void parseTargetFiles() {

        final Thread[] threads = new Thread[Settings.getInstance().getThreadNumber()];
        final TargetFile[] files = DataManager.getInstance().getTargetFileManager().getFiles()
                .toArray(new TargetFile[0]);
        final AtomicInteger index = new AtomicInteger();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new TargetFileParser(files, index, out, err));
            MetricsToolSecurityManager.getInstance().addPrivilegeThread(threads[i]);
            threads[i].start();
        }

        //�S�ẴX���b�h���I���̂�҂�
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �N���X�C���\�b�h�C�t�B�[���h�Ȃǂ̒�`�𖼑O��������DAST �p�[�X�̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ��D
     */
    private void resolveDefinitions() {

        // �������N���X���}�l�[�W���C �N���X���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodManager = DataManager.getInstance().getMethodInfoManager();

        // �e�������N���X�ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassManager.getClassInfos()) {

            final FileInfo fileInfo = unresolvedClassInfo.getFileInfo();

            //�@�N���X��������
            final TargetClassInfo classInfo = unresolvedClassInfo.resolve(null, null, classManager,
                    fieldManager, methodManager);

            fileInfo.addDefinedClass(classInfo);

            // �������ꂽ�N���X����o�^
            classManager.add(classInfo);
        }

        // �e�������N���X�̊O���̃��j�b�g������
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassManager.getClassInfos()) {
            unresolvedClassInfo.resolveOuterUnit(classManager);
        }
    }

    /**
     * �N���X�Ȃǂ̒�`�̒��ŗ��p����Ă���^�𖼑O��������D
     * resolveDefinitions�̌�ɌĂяo����Ȃ���΂Ȃ�Ȃ�
     */
    private void resolveTypes() {

        // �������N���X���}�l�[�W���C �N���X���}�l�[�W�����擾
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

        // ��� superTyp��������
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            unresolvedClassInfo.resolveSuperClass(classInfoManager);
        }

        // �c���Type������
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            unresolvedClassInfo.resolveTypeParameter(classInfoManager);

            for (final UnresolvedMethodInfo unresolvedMethod : unresolvedClassInfo
                    .getDefinedMethods()) {
                unresolvedMethod.resolveParameter(classInfoManager);
                unresolvedMethod.resolveReturnType(classInfoManager);
                unresolvedMethod.resolveThrownException(classInfoManager);
                unresolvedMethod.resolveTypeParameter(classInfoManager);
            }

            for (final UnresolvedConstructorInfo unresolvedConstructor : unresolvedClassInfo
                    .getDefinedConstructors()) {
                unresolvedConstructor.resolveParameter(classInfoManager);
                unresolvedConstructor.resolveThrownException(classInfoManager);
                unresolvedConstructor.resolveTypeParameter(classInfoManager);
            }

            for (final UnresolvedFieldInfo unresolvedField : unresolvedClassInfo.getDefinedFields()) {
                unresolvedField.resolveType(classInfoManager);
            }
        }
    }

    /**
     * ���\�b�h�I�[�o�[���C�h�����eMethodInfo�ɒǉ�����DaddInheritanceInfomationToClassInfos �̌� ���� registMethodInfos
     * �̌�ɌĂяo���Ȃ���΂Ȃ�Ȃ�
     */
    private void addOverrideRelation() {

        // �S�Ă̊O���N���X�ɑ΂���
        for (final ExternalClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getExternalClassInfos()) {
            addOverrideRelation(classInfo);

        }

        // �S�Ă̑ΏۃN���X�ɑ΂���
        for (final TargetClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {
            addOverrideRelation(classInfo);
        }
    }

    /**
     * ���\�b�h�I�[�o�[���C�h�����eMethodInfo�ɒǉ�����D�����Ŏw�肵���N���X�̃��\�b�h�ɂ��ď������s��
     * 
     * @param classInfo �ΏۃN���X
     */
    private void addOverrideRelation(final ClassInfo classInfo) {

        // �e�e�N���X�ɑ΂���
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            // �e�ΏۃN���X�̊e���\�b�h�ɂ��āC�e�N���X�̃��\�b�h���I�[�o�[���C�h���Ă��邩�𒲍�
            for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {
                addOverrideRelation(superClassInfo, methodInfo);
            }
        }

        // �e�C���i�[�N���X�ɑ΂���
        for (InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            addOverrideRelation((ClassInfo) innerClassInfo);
        }
    }

    /**
     * ���\�b�h�I�[�o�[���C�h����ǉ�����D�����Ŏw�肳�ꂽ�N���X�Œ�`����Ă��郁�\�b�h�ɑ΂��đ�����s��.
     * AddOverrideInformationToMethodInfos()�̒�����̂݌Ăяo�����D
     * 
     * @param classInfo �N���X���
     * @param overrider �I�[�o�[���C�h�Ώۂ̃��\�b�h
     */
    private void addOverrideRelation(final ClassInfo classInfo, final MethodInfo overrider) {

        if ((null == classInfo) || (null == overrider)) {
            throw new IllegalArgumentException();
        }

        for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {

            // ���\�b�h�����Ⴄ�ꍇ�̓I�[�o�[���C�h����Ȃ�
            if (!methodInfo.getMethodName().equals(overrider.getMethodName())) {
                continue;
            }

            if (0 != methodInfo.compareArgumentsTo(overrider)) {
                continue;
            }

            // �I�[�o�[���C�h�֌W��o�^����
            overrider.addOverridee(methodInfo);
            methodInfo.addOverrider(overrider);

            // ���ڂ̃I�[�o�[���C�h�֌W�������o���Ȃ��̂ŁC���̃N���X�̐e�N���X�͒������Ȃ�
            return;
        }

        // �e�N���X�Q�ɑ΂��čċA�I�ɏ���
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            addOverrideRelation(superClassInfo, overrider);
        }
    }

    /**
     * �G���e�B�e�B�i�t�B�[���h��N���X�j�̑���E�Q�ƁC���\�b�h�̌Ăяo���֌W��ǉ�����D
     */
    private void addMethodInsideInfomation() {

        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // �e�������N���X��� �ɑ΂���
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();

            // �������t�B�[���h���ɑ΂���
            for (final UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo
                    .getDefinedFields()) {
                final TargetFieldInfo fieldInfo = unresolvedFieldInfo.getResolved();
                if (null != unresolvedFieldInfo.getInitilizer()) {
                    final CallableUnitInfo initializerUnit = fieldInfo.isInstanceMember() ? classInfo
                            .getImplicitInstanceInitializer() : classInfo
                            .getImplicitStaticInitializer();
                    final ExpressionInfo initializerExpression = unresolvedFieldInfo
                            .getInitilizer().resolve(classInfo, initializerUnit, classInfoManager,
                                    fieldInfoManager, methodInfoManager);
                    fieldInfo.setInitializer(initializerExpression);

                    // regist as an initializer
                    // TODO need more SMART way
                    final LocalVariableInfo fieldInfoAsLocalVariable = new LocalVariableInfo(
                            fieldInfo.getModifiers(), fieldInfo.getName(), fieldInfo.getType(),
                            initializerUnit, fieldInfo.getFromLine(), fieldInfo.getFromColumn(),
                            fieldInfo.getToLine(), fieldInfo.getToColumn());
                    final LocalVariableUsageInfo fieldUsage = LocalVariableUsageInfo.getInstance(
                            fieldInfoAsLocalVariable, false, true, initializerUnit,
                            fieldInfo.getFromLine(), fieldInfo.getFromColumn(),
                            fieldInfo.getToLine(), fieldInfo.getToColumn());
                    final VariableDeclarationStatementInfo implicitInitializerStatement = new VariableDeclarationStatementInfo(
                            initializerUnit, fieldUsage, initializerExpression,
                            fieldInfo.getFromLine(), fieldInfo.getFromColumn(),
                            initializerExpression.getToLine(), initializerExpression.getToColumn());
                    initializerUnit.addStatement(implicitInitializerStatement);
                }
            }

            // �e���������\�b�h���ɑ΂���
            for (final UnresolvedMethodInfo unresolvedMethod : unresolvedClassInfo
                    .getDefinedMethods()) {

                final TargetMethodInfo method = unresolvedMethod.getResolved();
                unresolvedMethod.resolveInnerBlock(classInfo, method, classInfoManager,
                        fieldInfoManager, methodInfoManager);
            }

            // �e�������R���X�g���N�^���ɑ΂���
            for (final UnresolvedConstructorInfo unresolvedConstructor : unresolvedClassInfo
                    .getDefinedConstructors()) {

                final TargetConstructorInfo constructor = unresolvedConstructor.getResolved();
                unresolvedConstructor.resolveInnerBlock(classInfo, constructor, classInfoManager,
                        fieldInfoManager, methodInfoManager);
            }

            // resolve UnresolvedInstanceInitializers and register them
            for (final UnresolvedInstanceInitializerInfo unresolvedInstanceInitializer : unresolvedClassInfo
                    .getInstanceInitializers()) {

                final InstanceInitializerInfo initializer = unresolvedInstanceInitializer
                        .getResolved();
                unresolvedInstanceInitializer.resolveInnerBlock(classInfo, initializer,
                        classInfoManager, fieldInfoManager, methodInfoManager);
            }

            // resolve UnresolvedStaticInitializers and register them
            for (final UnresolvedStaticInitializerInfo unresolvedStaticInitializer : unresolvedClassInfo
                    .getStaticInitializers()) {

                final StaticInitializerInfo initializer = unresolvedStaticInitializer.getResolved();
                unresolvedStaticInitializer.resolveInnerBlock(classInfo, initializer,
                        classInfoManager, fieldInfoManager, methodInfoManager);
            }
        }
    }
}
