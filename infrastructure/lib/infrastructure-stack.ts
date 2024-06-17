import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as rds from 'aws-cdk-lib/aws-rds';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as iam from 'aws-cdk-lib/aws-iam';

export class InfrastructureStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        // Crear una VPC
        const vpc = new ec2.Vpc(this, 'MyVpc', {
            maxAzs: 2,
            natGateways: 1,
        });

        // Crear grupo de seguridad para la instancia EC2
        const ec2SecurityGroup = new ec2.SecurityGroup(this, 'Ec2SecurityGroup', {
            vpc: vpc,
            allowAllOutbound: true,
        });
        ec2SecurityGroup.addIngressRule(ec2.Peer.anyIpv4(), ec2.Port.tcp(8080), 'Allow HTTP traffic from anywhere');

        // Crear rol de instancia EC2 y perfil
        const ec2Role = new iam.Role(this, 'Ec2Role', {
            assumedBy: new iam.ServicePrincipal('ec2.amazonaws.com'),
            managedPolicies: [iam.ManagedPolicy.fromAwsManagedPolicyName('AmazonSSMManagedInstanceCore')],
        });

        // Crear una política de acceso a S3 para el rol de la instancia EC2
        const s3AccessPolicy = new iam.Policy(this, 'S3AccessPolicy', {
            policyName: 'S3AccessPolicy',
            statements: [
                new iam.PolicyStatement({
                    effect: iam.Effect.ALLOW,
                    actions: ['s3:GetObject', 's3:ListBucket'],
                    resources: [
                        `arn:aws:s3:::cdk-hnb659fds-assets-163752186135-eu-west-1`,
                        `arn:aws:s3:::cdk-hnb659fds-assets-163752186135-eu-west-1/*`
                    ],
                }),
            ],
        });
        s3AccessPolicy.attachToRole(ec2Role);

        // Crear instancia RDS para PostgreSQL
        const dbInstance = new rds.DatabaseInstance(this, 'PostgresDB', {
            engine: rds.DatabaseInstanceEngine.postgres({ version: rds.PostgresEngineVersion.VER_14 }),
            instanceType: ec2.InstanceType.of(ec2.InstanceClass.T3, ec2.InstanceSize.MICRO),
            vpc: vpc,
            vpcSubnets: { subnetType: ec2.SubnetType.PRIVATE_WITH_EGRESS },
            allocatedStorage: 20,
            databaseName: 'booklovers',
            credentials: rds.Credentials.fromPassword('postgres', cdk.SecretValue.plainText('adminBookLovers')),
            removalPolicy: cdk.RemovalPolicy.DESTROY,
            deletionProtection: false,
        });

        // Asegurar que la instancia EC2 puede conectarse a la instancia RDS
        dbInstance.connections.allowFrom(ec2SecurityGroup, ec2.Port.tcp(5432));

        // Datos de usuario para descargar y ejecutar la aplicación Spring Boot
        const bucketName = 'cdk-hnb659fds-assets-163752186135-eu-west-1';
        const jarFileName = 'book-lovers-0.0.1-SNAPSHOT.jar';
        const propertiesFileName = 'application-prod.properties';
        const dbEndpoint = dbInstance.instanceEndpoint.hostname;
        const userData = ec2.UserData.forLinux();
        userData.addCommands(
            'sudo yum update -y',
            'sudo tee /etc/yum.repos.d/corretto.repo <<EOF\n[corretto]\nname=Amazon Corretto\nbaseurl=https://yum.corretto.aws/x86_64\nenabled=1\ngpgcheck=1\ngpgkey=https://yum.corretto.aws/corretto.key\nEOF',
            'sudo yum install -y java-21-amazon-corretto-devel',
            'sudo yum install -y aws-cli',
            `sudo mkdir -p /opt/app`,
            `sudo aws s3 cp s3://${bucketName}/${jarFileName} /opt/app/book-lovers-0.0.1-SNAPSHOT.jar`,
            `sudo aws s3 cp s3://${bucketName}/${propertiesFileName} /opt/app/application-prod.properties`,
            `sudo chmod +x /opt/app/book-lovers-0.0.1-SNAPSHOT.jar`,
            `sudo chmod +r /opt/app/application-prod.properties`,
            'sudo touch /opt/app/application.log',
            'sudo chmod +w /opt/app/application.log',
            `sudo SPRING_DATASOURCE_URL="jdbc:postgresql://${dbEndpoint}:5432/booklovers" \
                SPRING_DATASOURCE_USERNAME="postgres" \
                SPRING_DATASOURCE_PASSWORD="adminBookLovers" \
                SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver" \
                SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect" \
                JWT_SECRET="bookloversSecretKeydefaultfeiorfjeioge9043i9340jgeirosdcmleifjwiorjr3920r92u490" \
                JWT_EXPIRATION_MS="86400000" \
                /usr/bin/java -jar /opt/app/book-lovers-0.0.1-SNAPSHOT.jar --spring.config.location=file:/opt/app/application-prod.properties --spring.profiles.active=prod > /opt/app/application.log 2>&1 &`
        );

        // Crear instancia EC2 pública
        new ec2.Instance(this, 'Instance', {
            vpc: vpc,
            instanceType: ec2.InstanceType.of(ec2.InstanceClass.T3, ec2.InstanceSize.MICRO),
            machineImage: ec2.MachineImage.latestAmazonLinux2(),
            securityGroup: ec2SecurityGroup,
            role: ec2Role,
            userData: userData,
            vpcSubnets: { subnetType: ec2.SubnetType.PUBLIC },
            associatePublicIpAddress: true,
        });
    }
}





