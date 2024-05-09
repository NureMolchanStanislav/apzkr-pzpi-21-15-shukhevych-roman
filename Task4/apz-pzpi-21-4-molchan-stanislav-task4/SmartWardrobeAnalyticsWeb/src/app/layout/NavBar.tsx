import { Button, Container, Menu } from "semantic-ui-react";
import 'semantic-ui-css/semantic.min.css';

export default function NavBar() {
    return (
        <Menu inverted fixed='top'>
            <Container>
                <Menu.Item header style={{ position: 'relative' }}>
                    <div style={{
                        width: '120px',
                        height: '120px',
                        position: 'absolute',
                        top: '0px',
                        left: '-150px',
                        overflow: 'hidden',
                        backgroundColor: 'white'
                    }}>
                        <img src="/assets/logo.png" alt="logo" style={{ width: '100%', height: '100%' }} />
                    </div>
                    Wardrobe
                </Menu.Item>
                <Menu.Item name="Collections"/>
                <Menu.Item>
                    <Button positive content='Create collection'/>
                </Menu.Item>
            </Container>
        </Menu>
    );
}