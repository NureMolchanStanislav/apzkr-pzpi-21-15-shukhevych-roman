/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import { Button, Container, Menu, Dropdown, DropdownMenu } from "semantic-ui-react";
import 'semantic-ui-css/semantic.min.css';
import { useEffect } from "react";
import { useStore } from "../stores/store";
import { Link } from "react-router-dom";
import LanguageSelector from "./LanguageSelector";
import { useTranslation } from "react-i18next";
import { exportToCsv } from "../api/agent";

export default observer(function NavBar() {
    const { userStore: { user, logout, isLoggedIn, getUser } } = useStore();
    const { t } = useTranslation();

    useEffect(() => {
        if (isLoggedIn && !user) {
            getUser();
        }
    }, [isLoggedIn, user, getUser]);

    const handleLogout = () => {
        console.log(user);
        logout();
        window.location.reload();
    };

    const isAdmin = user?.roles?.some(role => role.name === 'Admin');

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
                {!isAdmin && (
                    <>
                        {user?.roles?.some(role => role.name === 'Business') ? (
                            <Menu.Item as={Link} to='/brands' name='navbar.myBrands'>
                                {t('navbar.myBrands')}
                            </Menu.Item>
                        ) : (
                            <>
                                <Menu.Item as={Link} to='/collections' name='Collections'>
                                    {t('navbar.collections')}
                                </Menu.Item>
                                <Menu.Item>
                                    <Button positive content={t('navbar.createCollection')} />
                                </Menu.Item>
                            </>
                        )}
                    </>
                )}
                {isAdmin && (
                    <Menu.Item>
                        <Button as={Link} to="/admin" content={t('navbar.admin')} />
                    </Menu.Item>
                )}

                {isLoggedIn && user?.roles?.some(role => role.name === 'Admin') && (
                    <Menu.Item>
                        <Dropdown text={t('navbar.export')} pointing='top left'>
                            <Dropdown.Menu>
                                <Dropdown.Item onClick={() => exportToCsv('Users')}>Users</Dropdown.Item>
                                <Dropdown.Item onClick={() => exportToCsv('Collections')}>Collections</Dropdown.Item>
                                <Dropdown.Item onClick={() => exportToCsv('Itmes')}>Itmes</Dropdown.Item>
                                <Dropdown.Item onClick={() => exportToCsv('Brands')}>Brands</Dropdown.Item>
                                <Dropdown.Item onClick={() => exportToCsv('RFIDTags')}>RFIDTags</Dropdown.Item>
                                <Dropdown.Item onClick={() => exportToCsv('BrandBonuses')}>BrandBonuses</Dropdown.Item>
                                <Dropdown.Item onClick={() => exportToCsv('Usages')}>Usages</Dropdown.Item>
                                <Dropdown.Item onClick={() => exportToCsv('UsageHistory')}>UsageHistory</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                    </Menu.Item>
                )}
                <Menu.Item>
                    <LanguageSelector />
                </Menu.Item>

                {isLoggedIn || user != null ? (
                    <Menu.Item position='right'>
                        <Dropdown pointing='top left' text={user?.email}>
                            <DropdownMenu>
                                <Dropdown.Item as={Link} to={`/user/profile/${user?.id}`} text={t('navbar.myProfile')} icon='user' />
                                <Dropdown.Item as={Link} to={`/user/offers`} text={t('navbar.myOffers')} />
                                {user?.roles?.some(role => role.name === 'Business') && !isAdmin && (
                                    <>
                                        <Dropdown.Item>
                                            <Dropdown text={t('navbar.statistics')}>
                                                <DropdownMenu>
                                                    <Dropdown.Item as={Link} to={`/statistics/combo`} text={t('navbar.comboStatistics')} />
                                                    <Dropdown.Item as={Link} to={`/statistics/top`} text={t('navbar.topStatistics')} />
                                                    <Dropdown.Item as={Link} to={`/statistics/seasonal`} text={t('navbar.seasonalStatistics')} />
                                                </DropdownMenu>
                                            </Dropdown>
                                        </Dropdown.Item>
                                        <Dropdown.Item as={Link} to={`/bonus-system`} text={t('navbar.bonusSystem')} />
                                    </>
                                )}
                                <Dropdown.Item onClick={handleLogout} text={t('navbar.logout')} />
                            </DropdownMenu>
                        </Dropdown>
                    </Menu.Item>
                ) : (
                    <Menu.Item position='right'>
                        <Button as={Link} to="/login" content={t('navbar.login')} />
                        <Button as={Link} to="/register" content={t('navbar.register')} style={{ marginLeft: '0.5em' }} />
                    </Menu.Item>
                )}
            </Container>
        </Menu>
    );
});
