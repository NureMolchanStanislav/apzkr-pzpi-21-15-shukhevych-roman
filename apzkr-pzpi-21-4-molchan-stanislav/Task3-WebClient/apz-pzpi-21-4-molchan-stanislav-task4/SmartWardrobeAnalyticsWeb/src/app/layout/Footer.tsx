// src/components/Footer.tsx
import React from 'react';
import { Container, Segment } from 'semantic-ui-react';
import { useTranslation } from 'react-i18next';

const Footer = () => {
    const { t } = useTranslation();

    const footerStyles = {
        padding: '2em 0em',
        marginTop: 'auto',
        position: 'relative',
        bottom: 0,
        width: '100%',
    };

    return (
        <Segment inverted vertical style={footerStyles}>
            <Container textAlign='center'>
                <p>{t('footer.copyright')}</p>
            </Container>
        </Segment>
    );
};

export default Footer;
