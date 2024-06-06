import { useTranslation } from 'react-i18next';
import { Container, Header, Segment, List, Icon } from "semantic-ui-react";

export default function HomePage() {
    const { t } = useTranslation();

    return (
        <Container style={{ marginTop: '7em' }}>
            <Header as='h1' textAlign='center'>
                {t('home.welcome')}
            </Header>
            <Segment raised>
                <Header as='h2'>
                    <Icon name='info circle' />
                    <Header.Content>{t('home.aboutTitle')}</Header.Content>
                </Header>
                <p>
                    {t('home.aboutDescription')}
                </p>
            </Segment>
            <Segment raised>
                <Header as='h2'>
                    <Icon name='tasks' />
                    <Header.Content>{t('home.featuresTitle')}</Header.Content>
                </Header>
                <List bulleted>
                    <List.Item>{t('home.feature1')}</List.Item>
                    <List.Item>{t('home.feature2')}</List.Item>
                    <List.Item>{t('home.feature3')}</List.Item>
                    <List.Item>{t('home.feature4')}</List.Item>
                    <List.Item>{t('home.feature5')}</List.Item>
                </List>
            </Segment>
            <Segment raised>
                <Header as='h2'>
                    <Icon name='users' />
                    <Header.Content>{t('home.businessTitle')}</Header.Content>
                </Header>
                <p>
                    {t('home.businessDescription')}
                </p>
            </Segment>
            <Segment raised>
                <Header as='h2'>
                    <Icon name='shield' />
                    <Header.Content>{t('home.securityTitle')}</Header.Content>
                </Header>
                <p>
                    {t('home.securityDescription')}
                </p>
            </Segment>
        </Container>
    );
}
