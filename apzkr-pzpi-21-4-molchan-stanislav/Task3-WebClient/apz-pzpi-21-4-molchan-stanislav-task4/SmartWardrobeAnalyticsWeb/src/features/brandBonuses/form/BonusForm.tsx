/* eslint-disable react-refresh/only-export-components */
import { observer } from "mobx-react-lite";
import React, { useState, useEffect, ChangeEvent } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Segment, Button, Form, Dropdown } from "semantic-ui-react";
import LoadingComponents from "../../../app/layout/LoadingComponents";
import { useStore } from "../../../app/stores/store";

export default observer(function BonusForm() {
    const { bonusStore, brandStore } = useStore();
    const { selectedBonus, loadBonus, createBonus, updateBonus, loading: bonusLoading, loadingInitial: bonusLoadingInitial } = bonusStore;
    const { brands, loadForUserBrands, loading: brandLoading, loadingInitial: brandLoadingInitial } = brandStore;
    
    const navigate = useNavigate();
    const { id } = useParams();

    const [bonus, setBonus] = useState({
        conditionalNumberOfUsages: 0,
        conditionalDiscount: 0,
        conditionalComplexity: 0,
        maxDiscount: 0,
        brandId: ''
    });

    const [error, setError] = useState('');

    useEffect(() => {
        if (brands.length === 0) loadForUserBrands();
        if (id) {
            loadBonus(id).then(loadedBonus => setBonus(loadedBonus || {
                conditionalNumberOfUsages: 0,
                conditionalDiscount: 0,
                conditionalComplexity: 0,
                maxDiscount: 0,
                brandId: ''
            })).catch(err => {
                console.error("Failed to load bonus:", err);
                setError("Failed to load bonus.");
            });
        }
    }, [id, loadBonus, loadForUserBrands, brands.length]);

    function handleSubmit() {
        const dataToSend = id ? {
            ...bonus,
            id
        } : {
            ...bonus,
        };
    
        const action = id ? updateBonus : createBonus;
        action(dataToSend).then(() => {
            navigate(`/bonus-system`);
        }).catch(err => {
            console.error("Failed to save bonus:", err);
            setError("Failed to save bonus. Check your data.");
        });
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = event.target;
        setBonus({ ...bonus, [name]: value });
    }

    function handleDropdownChange(e: React.SyntheticEvent<HTMLElement, Event>, data: any) {
        setBonus({ ...bonus, [data.name]: data.value });
    }

    if (bonusLoadingInitial || brandLoadingInitial) return <LoadingComponents content="Loading data..." />;
    if (error) return <Segment clearing><div>Error: {error}</div></Segment>;

    const brandOptions = brands.map(brand => ({ key: brand.id, text: brand.name, value: brand.id }));

    return (
        <Segment clearing>
            <Form onSubmit={handleSubmit} autoComplete="off">
                <Form.Input 
                    placeholder='Conditional Number of Usages' 
                    value={bonus.conditionalNumberOfUsages} 
                    name='conditionalNumberOfUsages' 
                    onChange={handleChange} 
                />
                <Form.Input 
                    placeholder='Conditional Discount' 
                    value={bonus.conditionalDiscount} 
                    name='conditionalDiscount' 
                    onChange={handleChange} 
                />
                <Form.Input 
                    placeholder='Conditional Complexity' 
                    value={bonus.conditionalComplexity} 
                    name='conditionalComplexity' 
                    onChange={handleChange} 
                />
                <Form.Input 
                    placeholder='Max Discount' 
                    value={bonus.maxDiscount} 
                    name='maxDiscount' 
                    onChange={handleChange} 
                />
                <Form.Dropdown 
                    placeholder='Select Brand' 
                    fluid 
                    selection 
                    options={brandOptions} 
                    name='brandId' 
                    value={bonus.brandId}
                    onChange={handleDropdownChange} 
                />
                <Button loading={bonusLoading} floated="right" positive type='submit' content='Submit' />
                <Button floated="right" color='blue' onClick={() => navigate('/bonuses')} content='Cancel' />
            </Form>
        </Segment>
    );
});
